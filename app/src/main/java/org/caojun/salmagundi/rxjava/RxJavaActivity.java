package org.caojun.salmagundi.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.socks.library.KLog;

import org.caojun.salmagundi.BaseActivity;
import org.caojun.salmagundi.R;
import org.caojun.salmagundi.rxjava.data.Course;
import org.caojun.salmagundi.rxjava.data.Student;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * RxJava示例
 * Created by CaoJun on 2017/1/19.
 */

public class RxJavaActivity extends BaseActivity {

    private TextView tvInfo;
    private List<Student> students;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_rxjava);
        tvInfo = (TextView) this.findViewById(R.id.tvInfo);

        initData();
        doRxJava();
    }

    private void initData() {
        String[] NameCourse = new String[]{"语文","数学","英语","物理","化学"};
        String[] NameStudent = new String[]{"张三","李四","王五","赵六"};
        students = new ArrayList();
        for(int i = 0;i < NameStudent.length;i ++) {
            List<Course> courses = new ArrayList();
            for(int j = 0;j < i + 2;j ++) {
                Course course = new Course();
                course.setId(String.valueOf(j));
                course.setName(NameCourse[j]);
                courses.add(course);
            }
            Student student = new Student();
            student.setId(String.valueOf(i));
            student.setName(NameStudent[i]);
            student.setCourses(courses);
            students.add(student);
        }
    }

    private void showInfo(String text) {
        String info = tvInfo.getText().toString();
        tvInfo.setText(info + "\n" + text);
    }

    private void doRxJava() {
        Subscriber<Course> subscriber = new Subscriber<Course>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Course course) {
                showInfo("Course " + course.getId() + " : " + course.getName());
            }
        };
        Observable.from(students)
                .flatMap(new Func1<Student, Observable<Course>>() {
                    @Override
                    public Observable<Course> call(Student student) {
                        showInfo("\nStudent " + student.getId() + " : " + student.getName());
                        return Observable.from(student.getCourses());
                    }
                })
                .subscribe(subscriber);
    }
}
