package com.ssafy.eoullim.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MatchWait implements Comparable<MatchWait>{
    private Long childId;
    private short grade;
    private short priority;

    @Builder
    public MatchWait(Long childId, short grade, short priority) {
        this.childId = childId;
        this.grade = grade;
        this.priority = priority;
    }

    @Override
    public int compareTo(MatchWait o){
        if (grade <= o.grade){
            if(grade == o.grade){
                if(this.priority <= o.priority){
                    return 1;
                }else{
                    return -1;
                }
            }
            return 1;
        }else{
            return -1;
        }
    }
}
