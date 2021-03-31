package com.sampleassignment.developertext.Common;

import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class Common {
    public  static  boolean stopService;
public  static Subject<Boolean> mObservable = PublishSubject.create();

}
