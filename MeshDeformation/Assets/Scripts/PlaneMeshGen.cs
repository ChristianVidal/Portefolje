using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UIElements;

[ExecuteInEditMode]
public class PlaneMeshGen : MonoBehaviour
{
    public float width = 1.0f;
    public float length = 1.0f;
    public int widthSegments = 4;
    public int lengthSegments = 4;

    private void Start()
    {
        GeneratePlane();
    }

    private void Update()
    {
        
    }

    void GeneratePlane()
    {
        List<Vector3> vertices = new List<Vector3>();
        List<Vector2> uvs = new List<Vector2>();
        List<Vector3> normals = new List<Vector3>();
        List<int> triangles = new List<int>();

        GenerateVertices(vertices, uvs);
        GenerateTriangles(triangles);
        GenerateMesh(vertices, uvs, triangles);
    }

    void GenerateVertices(List<Vector3> vertices,List<Vector2> uvs)
    {
        for (int z = 0; z <= lengthSegments; z++)
        {
            for (int x = 0; x <= widthSegments; x++)
            {
                float posX = (x / (float)widthSegments) * width;
                float posZ = (z / (float)lengthSegments) * length;

                Vector3 vertex = new Vector3(posX, 0, posZ);
                vertices.Add(vertex);

                Vector2 uv = new Vector2(x / (float)widthSegments, z / (float)lengthSegments);
                uvs.Add(uv);
            }
        }
    }

    void GenerateTriangles(List<int> triangles)
    {
        int numVerticesX = widthSegments + 1;
        for (int z = 0; z < lengthSegments; z++)
        {
            for (int x = 0; x < widthSegments; x++)
            {
                int bottomLeft = z * numVerticesX + x;
                int bottomRight = bottomLeft + 1;
                int topLeft = bottomLeft + numVerticesX;
                int topRight = topLeft + 1;

                triangles.Add(bottomLeft);
                triangles.Add(topLeft);
                triangles.Add(topRight);

                triangles.Add(bottomLeft);
                triangles.Add(topRight);
                triangles.Add(bottomRight);
            }
        }
    }

    void GenerateMesh(List<Vector3> vertices, List<Vector2> uvs, List<int> triangles)
    {
        Mesh mesh = new Mesh();

        mesh.vertices = vertices.ToArray();
        mesh.uv = uvs.ToArray();
        mesh.triangles = triangles.ToArray();

        mesh.RecalculateBounds();
        mesh.RecalculateNormals();

        MeshFilter meshFilter = GetComponent<MeshFilter>();
        if (meshFilter == null) meshFilter = gameObject.AddComponent<MeshFilter>();
        meshFilter.mesh = mesh;

        MeshRenderer meshRenderer = GetComponent<MeshRenderer>();
        if (meshRenderer == null) meshRenderer = gameObject.AddComponent<MeshRenderer>();
    }

    void OnDrawGizmos()
    {
        if (GetComponent<MeshFilter>() == null) return;

        Mesh mesh = GetComponent<MeshFilter>().sharedMesh;
        if (mesh == null) return;

        Gizmos.color = Color.red;
        foreach (Vector3 vertex in mesh.vertices)
        {
            // Draw a small sphere at each vertex
            Gizmos.DrawSphere(transform.position + vertex, 0.025f);
        }

        Gizmos.color = Color.green;

        int[] tris = mesh.triangles;
        Vector3[] verts = mesh.vertices;

        for (int i = 0; i < tris.Length; i += 3)
        {
            Vector3 v0 = transform.position + verts[tris[i]];
            Vector3 v1 = transform.position + verts[tris[i + 1]];
            Vector3 v2 = transform.position + verts[tris[i + 2]];

            Gizmos.DrawLine(v0, v1);
            Gizmos.DrawLine(v1, v2);
            Gizmos.DrawLine(v2, v0);
        }
    }
}
