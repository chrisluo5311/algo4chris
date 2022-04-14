package com.algo4chris.algo4chrisservice.algorithm;

/**
 * Recursive Equation :
 * T(n) = 2T(n-1) + 1
 * T(1) = 1
 * ======================
 * T(n) = O(2^n-1) => O(2^n)
 *
 * */
public class Hanoi {

    public static void hanoiTower(int disks,
                                  char from_rod,
                                  char to_rod,
                                  char aux_rod){
        if(disks == 0){
            return;
        } else {
            //1.移動上方(n-1)的塔，從A移動到B，C為輔助
            hanoiTower(disks-1,from_rod,aux_rod,to_rod);
            //2.移動最底層的塔，從A移動到C，B為輔助
            System.out.println("移動第" + disks + "個碟子，從柱子" + from_rod + "到柱子" + to_rod);
            //3.移動B塔的(n-1)的塔，從B移動到C，A為輔助
            hanoiTower(disks - 1, aux_rod, to_rod, from_rod);
        }
    }

    public static void main(String[] args){
        int disks = 3;
        System.out.println("河內塔演算法start，一共" + disks + "個盤子");
        //從a到c 用b輔助
        hanoiTower(disks,'A','C', 'B');
    }

}
