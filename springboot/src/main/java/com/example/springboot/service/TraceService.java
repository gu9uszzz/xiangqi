package com.example.springboot.service;

import com.example.springboot.entity.Trace;
import com.example.springboot.mapper.TraceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraceService {

    @Autowired
    TraceMapper traceMapper;

    public void insertTrace(Trace trace) {
        traceMapper.insert(trace);
    }

    public List<Trace> selectTrace(Integer id) {
        return traceMapper.selectByTraceId(id);
    }

    public double selectMoney(Integer id) {
        return traceMapper.selectMoney(id);
    }

    /*public void updateTrace(Trace trace) {
        traceMapper.update(trace);
    }*/




}
