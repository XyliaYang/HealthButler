package com.mialab.healthbutler.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mialab.healthbutler.R;
import com.mialab.healthbutler.adapter.DeptAdapter;
import com.mialab.healthbutler.domain.Department;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wesly186 on 2016/6/9.
 */
public class HospitalDeptFragment extends Fragment {

    @BindView(R.id.rl_dept)
    RecyclerView rlDept;

    List<Department> departments = new ArrayList<Department>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hos_dept, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    private void initData() {

        departments.add(new Department(1,"http://img3.imgtn.bdimg.com/it/u=54127431,3038002068&fm=21&gp=0.jpg","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://m2.quanjing.com/2m/jui003/jui31237.jpg","外科","外科是研究外科疾病的发生，发展规律及其临床表现，诊断，预防和治疗的科学，是以手术切除、修补为主要治病手段的专业科室。随着显微外科技术的应用，外科得到了较大的发展。"));
        departments.add(new Department(1,"http://gk.cdbygk.com/zt/2015-01/js-xgj/images/x9_bz2.jpg","骨科","骨科是各大医院最常见的科室之一，主要研究骨骼肌肉系统的解剖、生理与病理，运用药物、手术及物理方法保持和发展这一系统的正常形态与功能。"));
        departments.add(new Department(1,"http://f.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=240404f5a9014c080d3620f76b12696d/d4628535e5dde7115dcfcaaaa7efce1b9d166149.jpg","消化科","消化科包括消化内科和消化外科，是各级医院为了诊疗消化系统疾病而设置的临床科室。治疗的疾病包括食管、胃、肠、肝、胆、胰以及腹膜、肠系膜、网膜等脏器的疾病。"));
        departments.add(new Department(1,"http://pic69.nipic.com/file/20150615/9885883_083427163000_2.jpg","呼吸科","呼吸，是指机体与外界环境之间气体交换的过程。人的呼吸过程包括三个互相联系的环节：外呼吸，包括肺通气和肺换气；气体在血液中的运输；内呼吸，指组织细胞与血液间的气体交换。"));
        departments.add(new Department(1,"http://img2.cache.netease.com/edu/2016/2/26/201602261037214f44d.jpg","儿科","儿科是全面研究小儿时期身心发育、保健以及疾病防治的综合医学科学。凡涉及儿童和青少年时期的健康与卫生问题都属于儿科范围。其医治对象处于生长发育期。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));
        departments.add(new Department(1,"http://","内科","内科学是临床医学的一个专科，几乎是所有其他临床医学的基础，亦有医学之母之称。内科学的内容包含了疾病的定义、病因、致病机转、流行病学、自然史、症状、征候、实验诊断、影像检查、鉴别诊断、诊断、治疗、预后。"));

        rlDept.setLayoutManager(new LinearLayoutManager(getActivity()));
        rlDept.setAdapter(new DeptAdapter(getActivity(), departments));
    }
}
