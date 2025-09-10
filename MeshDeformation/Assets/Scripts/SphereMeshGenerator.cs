using System.Collections.Generic;
using UnityEngine;

[ExecuteInEditMode]
public class SphereMeshGen : MonoBehaviour
{
    public float radius = 4.0f;
    public int latitudeSegments = 6;
    public int longitudeSegments = 6;

    private void Start()
    {
        GenerateSphere();
    }

    private void Update()
    {
        
    }

    void GenerateSphere()
    {
        List<Vector3> vertices = new List<Vector3>();
        List<Vector2> uvs = new List<Vector2>();
        List<Vector3> normals = new List<Vector3>();
        List<int> triangles = new List<int>();

        GenerateVertices(vertices, uvs);
        GenerateTriangles(triangles, vertices.Count);
        GenerateMesh(vertices, uvs, triangles);
    }

    void GenerateVertices(List<Vector3> vertices, List<Vector2> uvs)
    {
        vertices.Add(new Vector3(0, radius, 0));
        uvs.Add(new Vector2(0.5f, 1));

        for (int lat = 1; lat < latitudeSegments; lat++)
        {
            float phi = Mathf.PI * lat / latitudeSegments;

            for (int lon = 0; lon <= longitudeSegments; lon++)
            {
                float theta = 2 * Mathf.PI * lon / longitudeSegments;

                float x = radius * Mathf.Sin(phi) * Mathf.Cos(theta);
                float y = radius * Mathf.Cos(phi);
                float z = radius * Mathf.Sin(phi) * Mathf.Sin(theta);

                Vector3 vertex = new Vector3(x, y, z);
                vertices.Add(vertex);

                Vector2 uv = new Vector2(lon / (float)longitudeSegments, lat / (float)latitudeSegments);
                uvs.Add(uv);
            }
        }

        vertices.Add(new Vector3(0, -radius, 0));
        uvs.Add(new Vector2(0.5f, 0));
    }

    void GenerateTriangles(List<int> triangles, int vertexCount)
    {
        int ringSize = longitudeSegments + 1;
        int topVertex = 0;
        int bottomVertex = vertexCount - 1;

        for (int lon = 0; lon < longitudeSegments; lon++)
        {
            triangles.Add(topVertex);
            triangles.Add(1 + lon);
            triangles.Add(1 + lon + 1);
        }

        for (int lat = 0; lat < latitudeSegments - 2; lat++)
        {
            int ringStart = 1 + lat * ringSize;
            int nextRingStart = ringStart + ringSize;

            for (int lon = 0; lon < longitudeSegments; lon++)
            {
                int current = ringStart + lon;
                int next = nextRingStart + lon;

                triangles.Add(current);
                triangles.Add(next);
                triangles.Add(next + 1);

                triangles.Add(current);
                triangles.Add(next + 1);
                triangles.Add(current + 1);
            }
        }

        int lastRingStart = bottomVertex - ringSize;
        for (int lon = 0; lon < longitudeSegments; lon++)
        {
            triangles.Add(bottomVertex);
            triangles.Add(lastRingStart + lon + 1);
            triangles.Add(lastRingStart + lon);
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