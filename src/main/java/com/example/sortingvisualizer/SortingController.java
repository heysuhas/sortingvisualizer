package com.example.sortingvisualizer;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sort")
public class SortingController {

    @PostMapping("/{algorithm}")
    public List<int[]> sort(@PathVariable String algorithm, @RequestBody int[] array) {
        List<int[]> steps = new ArrayList<>();
        steps.add(array.clone());

        switch (algorithm) {
            case "bubble":
                bubbleSort(array, steps);
                break;
            case "selection":
                selectionSort(array, steps);
                break;
            case "insertion":
                insertionSort(array, steps);
                break;
            case "merge":
                mergeSort(array, 0, array.length - 1, steps);
                break;
            case "quick":
                quickSort(array, 0, array.length - 1, steps);
                break;
        }

        return steps;
    }

    private void bubbleSort(int[] arr, List<int[]> steps) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    steps.add(arr.clone());
                }
            }
        }
    }

    private void selectionSort(int[] arr, List<int[]> steps) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
            steps.add(arr.clone());
        }
    }

    private void insertionSort(int[] arr, List<int[]> steps) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
                steps.add(arr.clone());
            }
            arr[j + 1] = key;
            steps.add(arr.clone());
        }
    }

    private void mergeSort(int[] arr, int l, int r, List<int[]> steps) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m, steps);
            mergeSort(arr, m + 1, r, steps);
            merge(arr, l, m, r, steps);
        }
    }

    private void merge(int[] arr, int l, int m, int r, List<int[]> steps) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(arr, l, L, 0, n1);
        System.arraycopy(arr, m + 1, R, 0, n2);

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            k++;
            steps.add(arr.clone());
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
            steps.add(arr.clone());
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
            steps.add(arr.clone());
        }
    }

    private void quickSort(int[] arr, int low, int high, List<int[]> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSort(arr, low, pi - 1, steps);
            quickSort(arr, pi + 1, high, steps);
        }
    }

    private int partition(int[] arr, int low, int high, List<int[]> steps) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                steps.add(arr.clone());
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        steps.add(arr.clone());
        return i + 1;
    }
}