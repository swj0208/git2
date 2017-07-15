package com.yc.biz.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yc.bean.Student;
import com.yc.biz.StudentBiz;
import com.yc.util.DbHelper;

public class StudentBizImpl implements StudentBiz {
	private DbHelper db=new DbHelper();
	@Override
	public Student login(Student student) throws Exception {
		String sql="select * from student where name=? and pwd=?";
		List<Object> params=new ArrayList<Object>();
		params.add(student.getName());
		params.add(student.getPwd());
		List<Student> list = db.findObject(sql, params, Student.class);
		
		if(list == null || list.size()<=0){
			return null;
		}
		return list.get(0);
	}

}
