package com.dhar.automation.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dharmendra.singh
 */
@Entity
@Table(name = "schedule")
@NamedQueries({
        @NamedQuery(name = "findScheduleByEnvironment", query = "select s from Schedule s where s.environment = :eid order by s.id desc"),
        @NamedQuery(name = "countScheduleByEnvironment", query = "select count(*) from Schedule s where s.environment = :eid"),
        @NamedQuery(name = "findScheduleFullDetail", query = "select s " +
                                                            " from Schedule s " +
                                                            " left outer join fetch s.environment " +
                                                            " left outer join fetch s.executionList el " +
                                                            " left outer join fetch s.params params " +
                                                            " where s.id = :id" +
                                                            " order by el.id  "),
        /*@NamedQuery(name = "findScheduleByTestCaseAndEnvironment", query = "select s from Schedule s " +
                " where s.environment = :eid and s.runType='TESTCASE' and s.testCaseSuiteId = :testCaseId order by s.id desc"),*/
        @NamedQuery(name = "findScheduleById", query = "select s from Schedule s where s.id = :sid order by s.id desc "),
        /*@NamedQuery(name = "countScheduleByTestCaseAndEnvironment", query = "select count(*) from Schedule s where s.environment = :eid" +
                            " and s.runType='TESTCASE' and s.testCaseSuiteId = :testCaseId")*/
})
public class Schedule {

    public Schedule() {
    }

    public Schedule(Long scheduleId, Environment environment, String gridUrl, Map<String, String> params) {
        this.scheduleId = scheduleId;
        this.environment = environment;
        this.gridUrl = gridUrl;
        this.params = params;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(optional = false)
    private Environment environment;

    @Column(nullable = true, name = "grid_url")
    private String gridUrl;

    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="schedule_params", joinColumns=@JoinColumn(name="schedule_id"))
    private Map<String, String> params;

    @OneToMany(mappedBy = "schedule")
    private List<ExecutionList> executionList;

    @Column(name = "schedule_date")
    private Date scheduleDate = new Date();//this act like creation date for this entity

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }


    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public String getGridUrl() {
        return gridUrl;
    }

    public void setGridUrl(String gridUrl) {
        this.gridUrl = gridUrl;
    }


    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public List<ExecutionList> getExecutionList() {
        return executionList;
    }

    public void setExecutionList(List<ExecutionList> executionList) {
        this.executionList = executionList;
    }


    public String getTestCases() {
        return null;
    }

    public Map<String, String> getParamsMap(){
        /*Map<String, String> result = new HashMap<>();
        if(this.params == null){
            return result;
        }
        String[] params = this.params.split(";");

        for (String param : params) {
            if (StringUtils.isNotEmpty(param)) {
                String[] variable = param.split("=");
                String key = variable[0];
                String value = variable[1];
                result.put(key, value);
            }

        }
        return result;*/

        return this.params == null ? new HashMap<String, String>() : this.params;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", scheduleId=" + scheduleId +
                ", environment=" + environment.getId() +
                ", gridUrl='" + gridUrl + '\'' +
                ", params='" + params + '\'' +
                '}';
    }
}
