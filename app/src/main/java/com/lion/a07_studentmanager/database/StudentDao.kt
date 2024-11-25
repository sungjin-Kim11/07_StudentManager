package com.lion.a07_studentmanager.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StudentDao {
    // 학생정보 저장
    @Insert
    fun insertStudentData(studentVO: StudentVO)

    // 학생 정보 목록을 가져온다.
    // select * from StudentTable
    // StudentTable에서 모든 컬럼의 데이터를 가져온다.
    // 조건이 없으므로 모든 행들을 가져온다.

    // order by 컬럼명 : 컬럼명을 기준으로 행을 오름 차순 정렬한다.
    // order by 컬럼명 desc : 컬럼명을 기준으로 행을 내림 차순 정렬한다.
    @Query("""
        select * from StudentTable
        order by studentIdx desc
    """)
    fun selectStudentDataAll():List<StudentVO>
}