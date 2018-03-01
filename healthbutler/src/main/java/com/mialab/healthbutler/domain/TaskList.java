package com.mialab.healthbutler.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Wesly186 on 2016/3/30.
 */
public class TaskList {

    public ArrayList<Task> mUncompleteTaskList;
    public ArrayList<Task> mCompleteTaskList;

    public class Task implements Serializable {
        int mId;
        String mTaskDetail;
        String mTaskDate;
        int mRemindTime;
        int mRemindUnit;
        String mTaskType;
        String mRemarks;

        public int getmId() {
            return mId;
        }

        public void setmId(int mId) {
            this.mId = mId;
        }

        public String getmTaskDetail() {
            return mTaskDetail;
        }

        public void setmTaskDetail(String mTaskDetail) {
            this.mTaskDetail = mTaskDetail;
        }

        public String getmTaskDate() {
            return mTaskDate;
        }

        public void setmTaskDate(String mTaskDate) {
            this.mTaskDate = mTaskDate;
        }

        public int getmRemindTime() {
            return mRemindTime;
        }

        public void setmRemindTime(int mRemindTime) {
            this.mRemindTime = mRemindTime;
        }

        public int getmRemindUnit() {
            return mRemindUnit;
        }

        public void setmRemindUnit(int mRemindUnit) {
            this.mRemindUnit = mRemindUnit;
        }

        public String getmTaskType() {
            return mTaskType;
        }

        public void setmTaskType(String mTaskType) {
            this.mTaskType = mTaskType;
        }

        public String getmRemarks() {
            return mRemarks;
        }

        public void setmRemarks(String mRemarks) {
            this.mRemarks = mRemarks;
        }
    }
}
