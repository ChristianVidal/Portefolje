using System.Collections.Generic;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.InputSystem;
using static UnityEditor.Searcher.SearcherWindow.Alignment;

public class MeshDeformationTool : MonoBehaviour
{
    private Vector3 hit;
    private Vector3 previousHit;
    private Vector3 samplePlane;
    private Vector3 sampleNormal;
    private Vector3 targetPlane;
    private Vector3 targetNormal;
    private Vector3 brushDirection;
    private Vector3 brushBitangent;
    private Mesh mesh;

    private Vector3[] vertices;
    private List<int> editVertices = new List<int>();

    public float EditRadius = 0.5f;
    public float SampleRadius = 1f;
    public float StepDistance = 0.1f;
    public float Power = 1f;
    public float Depth = 0.1f;

    private Texture2D alphaMask;

    // Start is called once before the first execution of Update after the MonoBehaviour is created
    void Start()
    {
        mesh = GetComponent<MeshFilter>().mesh;
        vertices = mesh.vertices;

        alphaMask = new Texture2D(2, 2);
        alphaMask.SetPixel(0, 0, Color.white);
        alphaMask.SetPixel(1, 0, Color.white);
        alphaMask.SetPixel(0, 1, Color.white);
        alphaMask.SetPixel(1, 1, Color.white);
        alphaMask.Apply();
    }

    // Update is called once per frame
    void Update()
    {
        if (Mouse.current.leftButton.isPressed)
        {
            if (HitDetected())
            {   
                SelectionVolume(hit);
                ApplyMask();
                RecalculateMesh(mesh);
            }
        }
    }

    bool HitDetected()
    {
        Ray ray = Camera.main.ScreenPointToRay(Mouse.current.position.ReadValue());
        RaycastHit hitPos = new RaycastHit();

        if (!Physics.Raycast(ray, out hitPos))
        {
            Debug.Log("No mesh hit. Please click on a mesh to deform.");
            return false;
        }

        hit = transform.InverseTransformPoint(hitPos.point);
        float distance = Vector3.Distance(hit, previousHit);

        if (previousHit == Vector3.zero)
        {
            previousHit = hit;
            return false;
        }

        if (distance < StepDistance)
        {
            return false;
        }

        brushDirection = (hit - previousHit).normalized;

        int steps = Mathf.FloorToInt(distance / StepDistance);

        for (int s = 1; s < steps; s++)
        {
            float alpha = (float)s / steps;
            Vector3 interpolateHit = Vector3.Lerp(previousHit, hit, alpha);
            SelectionVolume(interpolateHit);
            ApplyMask();
            RecalculateMesh(mesh);

        }

        previousHit = hit;

        return true;
    }

    void SelectionVolume(Vector3 hit)
    {
        int sampleCount = 0;

        samplePlane = Vector3.zero;
        sampleNormal = Vector3.zero;

        editVertices.Clear();

        for (int v = 0; v < vertices.Length; v++)
        {
            Vector3 vertex = vertices[v];
            Vector3 normal = mesh.normals[v];

            float distance = Vector3.Distance(vertex, hit);

            if (distance <= SampleRadius)
            {
                samplePlane += vertex;
                sampleNormal += normal;
                sampleCount++;
            }

            if (distance <= EditRadius)
            {
                editVertices.Add(v);
            }
        }

        //Create SamplePlane & TargetPlane
        if (sampleCount > 0)
        {
            samplePlane /= sampleCount;
            sampleNormal = sampleNormal.normalized;

            targetPlane = samplePlane + sampleNormal * Depth;
            targetNormal = sampleNormal;

            brushBitangent = Vector3.Normalize(Vector3.Cross(sampleNormal, brushDirection));
        }
    }

    void ApplyMask()
    {
        for (int i = 0; i < editVertices.Count; i++)
        {
            int vertexIndex = editVertices[i];
            Vector3 vertex = vertices[vertexIndex];

            float signedDistance = Vector3.Dot(vertex - targetPlane, targetNormal);

            if (signedDistance > 0f)
            {
                continue;
            }

            Vector3 toVertex = vertex - samplePlane;
            float u = Vector3.Dot(toVertex, brushDirection) / (2 * EditRadius) + 0.5f;
            float v = Vector3.Dot(toVertex, brushBitangent) / (2 * EditRadius) + 0.5f;
            u = Mathf.Clamp01(u);
            v = Mathf.Clamp01(v);

            float weight = alphaMask.GetPixelBilinear(u, v).r;
            if (weight <= 0f)
            {
                continue;
            }

            Vector3 displacement = targetNormal * Depth * weight * Power;
            vertex += displacement;

            vertices[vertexIndex] = vertex;
        }
    }

    void RecalculateMesh(Mesh mesh)
    {
        mesh.vertices = vertices;
        mesh.RecalculateNormals();
        mesh.RecalculateBounds();
    }
}
