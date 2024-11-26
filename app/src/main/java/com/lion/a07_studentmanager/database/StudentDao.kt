package com.lion.a07_studentmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

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

    // where studentName = :studentName
    // studentName 컬럼의 값이 지정된 값과 같은 행만 가져온다.
    @Query("""
        select * from StudentTable
        where studentName = :studentName
        order by studentIdx desc
    """)
    fun selectStudentDataAllByStudentName(studentName:String):List<StudentVO>

    // 학생 번호와 같은 학생의 데이터를 반환한다.
    // where studentIdx = 값
    // 테이블에 있는 행들 중에 studentIdx 컬럼의 값이 지정된 값과 같은 행들만 가져온다.
    @Query("""
        select * from StudentTable
        where studentIdx = :studentIdx
    """)
    fun selectStudentDataByStudentIdx(studentIdx:Int):StudentVO

    // 학생 정보를 삭제한다.
    // 매개변수로 받은 객체가 가지고 있는 프로퍼티 중에 primary key 프로퍼티가 조건식이 된다.
    @Delete
    fun deleteStudentData(studentVO: StudentVO)

    // 학생 정보를 수정한다.
    // 매개변수로 받은 객체가 가지고 있는 프로퍼티 중에 primary key 프로퍼티가 조건식이 된다.
    @Update
    fun updateStudentData(studentVO: StudentVO)
}