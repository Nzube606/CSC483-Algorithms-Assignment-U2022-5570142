# CSC483-Algorithms-Assignment-U2022-5570142
# 📘 Algorithms Assignment Repository

## 📌 Overview
This repository contains two projects:
1. Search Optimization Project
2. Sorting Algorithms project

---
# 🔹 Project 1: Search Optimization

## Description
This project focuses on optimizing search algorithms and comparing their efficiency.

## Algorithms Implemented
- Linear Search
- Binary Search
- (Add any others you used)

## Features
- Performance comparison of search techniques
- Analysis based on input size and structure
- Optimization techniques applied

## Compilation

## Execution

## 🧪 JUnit Testing

JUnit 5 is used for testing.

### 📌 Tested Features
The following functionalities are tested:

- Sequential search by product ID
- Binary search by product ID
- Hybrid search using name lookup (HashMap)
- Sorted insertion in the hybrid product catalog

---

### 🔍 Test Description

**Sequential Search**
- Confirms that an existing product ID is found correctly
- Ensures that searching for a non-existent ID returns `null`

**Binary Search**
- Verifies correct operation on a sorted array
- Confirms accurate retrieval of existing elements
- Ensures proper handling of missing elements

*Note: Binary search requires the array to be sorted before execution.*

---

**Hybrid Search (Name-Based)**
- Tests fast lookup using a HashMap
- Confirms products can be retrieved by name
- Ensures non-existent names return `null`

---

**Sorted Insertion**
- Verifies that `addProduct()` maintains sorted order internally
- Confirms binary search works correctly after insertions
- Tests insertion of elements in non-sorted order

---

### ▶️ How to Run Tests

1. Open the project in IntelliJ IDEA
2. Navigate to:
   src/test/java/Search_Optimization/
3. Right-click on `ProductSearchTest`
4. Click **Run 'ProductSearchTest'**

---

### ✅ Expected Result

All tests should pass successfully, confirming:
- Correct implementation of search algorithms
- Proper maintenance of sorted data
- Reliable hybrid search functionality

---

### 📊 Testing Importance

These tests ensure that:
- The algorithms are implemented correctly
- Edge cases are handled properly
- The system behaves reliably under different conditions

---


# 🔹 Project 2: Sorting Algorithms project

## Description
This project evaluates the performance of multiple sorting algorithms using empirical analysis.

## Algorithms Implemented
- Insertion Sort
- Merge Sort
- Quick Sort

## Features
- Dataset generation (random, sorted, reverse, nearly sorted, duplicates)
- Performance measurement (time, comparisons, swaps)
- Statistical analysis (mean, standard deviation, t-test)
- Decision tree for algorithm selection

## Compilation

## Execution

## 🧪 Testing

JUnit 5 is used for testing.

### Test Coverage Includes:
- Random datasets
- Sorted datasets
- Reverse sorted datasets
- Nearly sorted datasets
- Duplicate values
- Edge cases (empty array, single element)


---

# 📦 Dependencies

- Java (JDK 8 or higher)
- (Optional) Apache Commons Math for statistical analysis

---

# 🧪 Sample Usage

Run each project separately depending on the main class:

---

# ⚠️ Known Limitations

- Results may vary depending on system performance
- Limited dataset sizes tested
- Statistical analysis based on small sample size

---

# 📊 Notes

- Both projects were implemented as part of an academic assignment
- Emphasis was placed on empirical evaluation and algorithm efficiency

---