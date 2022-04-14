package com.algo4chris.algo4chrisservice.algorithm;

/**
 * 選擇排序法的複雜度為O(n^2)，和氣泡排序法是一樣的
 *
 *
 * */
public class SelectionSort {

    public static void selectionSort(int[] array){
        System.out.println("數組排序前:");
        printArray(array);
        //n
        for (int i = 0 ; i < array.length-1 ; i++){ // -1目的避免讓下面的j報ArrayIndexOutOfBoundsException
            int index = i;
            //n
            for(int j = i+1 ; j < array.length;j++){
                if(array[j] < array[index]){
                    index = j;
                }
            }
            int temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
        System.out.println("\n數組排序後:");
        printArray(array);
    }

    static void printArray(int[] arr) {
        for (int j : arr) {
            System.out.print(j + " ");
        }
        System.out.println();
    }


    public static void main(String args[]){
        selectionSort(new int[]{10,5,16,9,13,2});
    }

}
