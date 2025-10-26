import java.io.*;
import java.util.*;

/**
 * MST Analysis - Assignment 3
 * Implementation of Prim's and Kruskal's algorithms for city transportation network optimization
 * Author: Chingiz Uraimov
 * Date: October 26, 2025
 */

// Class representing a graph edge
class Edge {
    String from;
    String to;
    int weight;
    
    Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

// Class for storing algorithm results
class Result {
    List<Edge> mstEdges = new ArrayList<>();
    int totalCost = 0;
    int operationsCount = 0;
    double executionTime = 0;
}

// Main class with MST algorithms implementation
public class MSTAnalysis {
    
    /**
     * Prim's Algorithm for finding Minimum Spanning Tree
     * Greedy approach: grows MST one vertex at a time
     * Time Complexity: O(E * V) for this implementation
     * 
     * @param nodes List of graph vertices (city districts)
     * @param edges List of graph edges (potential roads)
     * @return Result object containing MST edges, cost, operations count, and execution time
     */
    public static Result primAlgorithm(List<String> nodes, List<Edge> edges) {
        Result result = new Result();
        long startTime = System.nanoTime();
        
        // Set to track visited vertices
        Set<String> visited = new HashSet<>();
        visited.add(nodes.get(0)); // Start from the first vertex
        
        // Continue until all vertices are included in MST
        while (visited.size() < nodes.size()) {
            Edge minEdge = null;
            int minWeight = Integer.MAX_VALUE;
            
            // Find minimum weight edge connecting visited and unvisited vertices
            for (Edge edge : edges) {
                result.operationsCount++; // Count comparison operation
                
                // Check if edge connects visited and unvisited vertex
                if (visited.contains(edge.from) && !visited.contains(edge.to)) {
                    if (edge.weight < minWeight) {
                        minWeight = edge.weight;
                        minEdge = edge;
                    }
                } else if (visited.contains(edge.to) && !visited.contains(edge.from)) {
                    if (edge.weight < minWeight) {
                        minWeight = edge.weight;
                        minEdge = new Edge(edge.to, edge.from, edge.weight);
                    }
                }
            }
            
            // Add minimum edge to MST
            if (minEdge != null) {
                result.mstEdges.add(minEdge);
                result.totalCost += minEdge.weight;
                visited.add(minEdge.to);
            }
        }
        
        long endTime = System.nanoTime();
        result.executionTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        
        return result;
    }
    
    /**
     * Kruskal's Algorithm for finding Minimum Spanning Tree
     * Greedy approach: considers all edges sorted by weight
     * Uses Union-Find data structure to detect cycles
     * Time Complexity: O(E log E) for sorting + O(E * α(V)) for union-find
     * 
     * @param nodes List of graph vertices (city districts)
     * @param edges List of graph edges (potential roads)
     * @return Result object containing MST edges, cost, operations count, and execution time
     */
    public static Result kruskalAlgorithm(List<String> nodes, List<Edge> edges) {
        Result result = new Result();
        long startTime = System.nanoTime();
        
        // Sort edges by weight in ascending order
        List<Edge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort((e1, e2) -> Integer.compare(e1.weight, e2.weight));
        
        // Union-Find data structure for cycle detection
        Map<String, String> parent = new HashMap<>();
        for (String node : nodes) {
            parent.put(node, node); // Initially, each node is its own parent
        }
        
        // Find operation with path compression
        String find(String node) {
            result.operationsCount++;
            if (!parent.get(node).equals(node)) {
                parent.put(node, find(parent.get(node))); // Path compression
            }
            return parent.get(node);
        }
        
        // Union operation to merge two sets
        void union(String node1, String node2) {
            result.operationsCount++;
            String root1 = find(node1);
            String root2 = find(node2);
            if (!root1.equals(root2)) {
                parent.put(root1, root2);
            }
        }
        
        // Process edges in ascending order of weight
        for (Edge edge : sortedEdges) {
            result.operationsCount++;
            String root1 = find(edge.from);
            String root2 = find(edge.to);
            
            // Add edge to MST if it doesn't create a cycle
            if (!root1.equals(root2)) {
                result.mstEdges.add(edge);
                result.totalCost += edge.weight;
                union(edge.from, edge.to);
                
                // Stop when MST is complete (V-1 edges)
                if (result.mstEdges.size() == nodes.size() - 1) {
                    break;
                }
            }
        }
        
        long endTime = System.nanoTime();
        result.executionTime = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        
        return result;
    }
    
    /**
     * Main method - entry point of the program
     * Reads input JSON, runs both algorithms, generates output JSON
     */
    public static void main(String[] args) {
        try {
            // Read input JSON file
            String inputContent = readFile("ass_3_input.json");
            
            // Parse JSON manually (simple parsing without external libraries)
            List<GraphData> graphs = parseInput(inputContent);
            
            // Build output JSON structure
            StringBuilder output = new StringBuilder();
            output.append("{\n  \"results\": [\n");
            
            // Process each graph
            for (int i = 0; i < graphs.size(); i++) {
                GraphData graph = graphs.get(i);
                
                // Run Prim's algorithm
                Result primResult = primAlgorithm(graph.nodes, graph.edges);
                
                // Run Kruskal's algorithm
                Result kruskalResult = kruskalAlgorithm(graph.nodes, graph.edges);
                
                // Format JSON output for this graph
                output.append("    {\n");
                output.append("      \"graph_id\": ").append(graph.id).append(",\n");
                output.append("      \"input_stats\": {\n");
                output.append("        \"vertices\": ").append(graph.nodes.size()).append(",\n");
                output.append("        \"edges\": ").append(graph.edges.size()).append("\n");
                output.append("      },\n");
                
                // Prim's algorithm results
                output.append("      \"prim\": {\n");
                output.append("        \"mst_edges\": [\n");
                for (int j = 0; j < primResult.mstEdges.size(); j++) {
                    Edge e = primResult.mstEdges.get(j);
                    output.append("          {\"from\": \"").append(e.from).append("\", \"to\": \"")
                          .append(e.to).append("\", \"weight\": ").append(e.weight).append("}");
                    if (j < primResult.mstEdges.size() - 1) output.append(",");
                    output.append("\n");
                }
                output.append("        ],\n");
                output.append("        \"total_cost\": ").append(primResult.totalCost).append(",\n");
                output.append("        \"operations_count\": ").append(primResult.operationsCount).append(",\n");
                output.append("        \"execution_time_ms\": ").append(String.format("%.2f", primResult.executionTime)).append("\n");
                output.append("      },\n");
                
                // Kruskal's algorithm results
                output.append("      \"kruskal\": {\n");
                output.append("        \"mst_edges\": [\n");
                for (int j = 0; j < kruskalResult.mstEdges.size(); j++) {
                    Edge e = kruskalResult.mstEdges.get(j);
                    output.append("          {\"from\": \"").append(e.from).append("\", \"to\": \"")
                          .append(e.to).append("\", \"weight\": ").append(e.weight).append("}");
                    if (j < kruskalResult.mstEdges.size() - 1) output.append(",");
                    output.append("\n");
                }
                output.append("        ],\n");
                output.append("        \"total_cost\": ").append(kruskalResult.totalCost).append(",\n");
                output.append("        \"operations_count\": ").append(kruskalResult.operationsCount).append(",\n");
                output.append("        \"execution_time_ms\": ").append(String.format("%.2f", kruskalResult.executionTime)).append("\n");
                output.append("      }\n");
                output.append("    }");
                
                if (i < graphs.size() - 1) output.append(",");
                output.append("\n");
            }
            
            output.append("  ]\n}");
            
            // Write results to output file
            writeFile("ass_3_output.json", output.toString());
            
            System.out.println("✓ Algorithms executed successfully!");
            System.out.println("✓ Results saved to ass_3_output.json");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Helper class to store graph data
     */
    static class GraphData {
        int id;
        List<String> nodes;
        List<Edge> edges;
        
        GraphData(int id, List<String> nodes, List<Edge> edges) {
            this.id = id;
            this.nodes = nodes;
            this.edges = edges;
        }
    }
    
    /**
     * Read entire file content as string
     */
    static String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }
    
    /**
     * Write string content to file
     */
    static void writeFile(String filename, String content) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(content);
        writer.close();
    }
    
    /**
     * Simple JSON parser without external libraries
     * Parses input JSON structure with graphs, nodes, and edges
     */
    static List<GraphData> parseInput(String json) {
        List<GraphData> graphs = new ArrayList<>();
        
        // Split by graph blocks
        String[] graphBlocks = json.split("\"id\":");
        for (int i = 1; i < graphBlocks.length; i++) {
            String block = graphBlocks[i];
            
            // Extract graph ID
            int id = Integer.parseInt(block.substring(0, block.indexOf(",")).trim());
            
            // Extract nodes
            List<String> nodes = new ArrayList<>();
            String nodesSection = block.substring(block.indexOf("\"nodes\":") + 8);
            nodesSection = nodesSection.substring(nodesSection.indexOf("[") + 1, nodesSection.indexOf("]"));
            String[] nodeArray = nodesSection.split(",");
            for (String node : nodeArray) {
                nodes.add(node.trim().replace("\"", ""));
            }
            
            // Extract edges
            List<Edge> edges = new ArrayList<>();
            String edgesSection = block.substring(block.indexOf("\"edges\":") + 8);
            edgesSection = edgesSection.substring(edgesSection.indexOf("[") + 1, edgesSection.indexOf("]"));
            String[] edgeBlocks = edgesSection.split("\\},");
            
            for (String edgeBlock : edgeBlocks) {
                if (edgeBlock.contains("from")) {
                    String from = extractValue(edgeBlock, "from");
                    String to = extractValue(edgeBlock, "to");
                    int weight = Integer.parseInt(extractValue(edgeBlock, "weight"));
                    edges.add(new Edge(from, to, weight));
                }
            }
            
            graphs.add(new GraphData(id, nodes, edges));
        }
        
        return graphs;
    }
    
    /**
     * Extract value from JSON string by key
     */
    static String extractValue(String json, String key) {
        int startIndex = json.indexOf("\"" + key + "\":");
        if (startIndex == -1) return "";
        
        startIndex = json.indexOf(":", startIndex) + 1;
        String value = json.substring(startIndex).trim();
        
        if (value.startsWith("\"")) {
            value = value.substring(1, value.indexOf("\"", 1));
        } else {
            int endIndex = Math.min(
                value.indexOf(",") != -1 ? value.indexOf(",") : value.length(),
                value.indexOf("}") != -1 ? value.indexOf("}") : value.length()
            );
            value = value.substring(0, endIndex).trim();
        }
        
        return value;
    }
}
