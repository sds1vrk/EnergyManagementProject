package com.mir.ems.main;

public class Knapsack {
    
    static int[] weight = { 4,2,6,4,2,10 };    // 각 물건의 무게
    static int[] value = { 7,10,6,7,5,4 };    // 각 물건의 가치
    static int N = 6;                        // 물건의 종류
    
    public static void main(String[] args) {
 
        int capacity = 17;    // 배낭이 담을 수 있는 물건의 최대 무게
        int index = 0;        // 물건의 인덱스
        
        long begin = System.currentTimeMillis();
        
        // 물건을 담는 최대 가치 출력
        System.out.println(getMax(capacity, index));
        
        long end = System.currentTimeMillis();
        System.out.printf("### 처리시간 : %.3f(sec)\n", (end-begin)/1000.0);
    }
    
    private static int getMax(int capacity, int index) {
        int ret = 0;
        
        // 모든 물건을 검사했으면 종료
        if(index == N) {
            return 0;
        }
        
        // 물건을 담지 않는 경우
        int unselected = getMax(capacity, index+1);
        int selected = 0;
        
        // 여유공간이 있어서 물건을 담는 경우
        if(capacity >= weight[index]) {
            selected = getMax(capacity-weight[index], index+1) + value[index];
        }
        
        // 물건을 담지 않는 경우와 담는 경우 중에 최대값을 취함.
        ret = Math.max(unselected, selected);
 
        return ret;
    }
}
