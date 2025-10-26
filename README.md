# MST Algorithm Analysis - City Transportation Network Optimization

**Author:** Chingiz Uraimov  
**Date:** October 26, 2025

## Overview

This project implements and compares **Prim's** and **Kruskal's** algorithms for optimizing a city transportation network. The goal is to connect all districts with minimum construction cost using Minimum Spanning Tree (MST) algorithms.

## Results Summary

### Graph 1 (5 vertices, 7 edges)
| Algorithm | Total Cost | Operations | Time (ms) |
|-----------|-----------|------------|-----------|
| Prim      | 16        | 42         | ~1.5      |
| Kruskal   | 16        | 37         | ~1.3      |

### Graph 2 (4 vertices, 5 edges)
| Algorithm | Total Cost | Operations | Time (ms) |
|-----------|-----------|------------|-----------|
| Prim      | 6         | 29         | ~0.9      |
| Kruskal   | 6         | 31         | ~0.9      |

**Key Finding:** Both algorithms produce identical MST costs (as expected), but differ in performance characteristics.

## Algorithm Comparison

### Prim's Algorithm
- **Best for:** Dense graphs (many edges)
- **Approach:** Grows MST one vertex at a time
- **Time Complexity:** O(E log V)
- **Advantage:** More efficient with adjacency matrix representation

### Kruskal's Algorithm
- **Best for:** Sparse graphs (fewer edges)
- **Approach:** Processes all edges sorted by weight
- **Time Complexity:** O(E log E)
- **Advantage:** Simpler implementation, works well with edge list format

## Performance Analysis

**Operation Count:**
- Kruskal performed 12% fewer operations on Graph 1
- Prim performed 6% fewer operations on Graph 2
- Difference is minimal for small graphs

**Execution Time:**
- Both algorithms run in under 2ms for small graphs
- Performance difference becomes significant only for large networks (1000+ vertices)

## Practical Recommendations

**Use Kruskal when:**
- Working with sparse road networks
- Data comes as list of individual road proposals
- Need simple, easy-to-explain implementation

**Use Prim when:**
- Working with dense urban areas (many connections)
- Starting from specific district is important
- Using adjacency matrix representation

## Conclusions

Both algorithms successfully minimize construction costs. For this assignment's JSON-based edge list format, **Kruskal's algorithm** proved slightly more efficient and straightforward to implement. However, algorithm choice matters most for very large networks - for small city networks like these examples, either algorithm works perfectly.

The identical total costs (16 and 6) confirm both implementations are correct, despite potentially different MST structures.

## Implementation Details

- **Language:** Java (no external libraries)
- **Input:** JSON file with graph data
- **Output:** JSON file with MST results, costs, and performance metrics
- **Features:** Operation counting, execution time measurement, simple JSON parsing

## How to Run

```bash
javac MSTAnalysis.java
java MSTAnalysis
```

Input: `ass_3_input.json`  
Output: `ass_3_output.json`

---

## References

1. Cormen et al. (2009). *Introduction to Algorithms* (3rd ed.), Chapter 23
2. Prim, R.C. (1957). "Shortest connection networks". *Bell System Technical Journal*
3. Kruskal, J.B. (1956). "On the shortest spanning subtree". *Proceedings of AMS*
