package com.fxq.designmethod.strategy;

/**
 * @author huiguo
 * @date 2019/1/16
 */
public class Context {
    private IStrategy strategy;

    public void setStrategy(IStrategy strategy){
        this.strategy = strategy;
    }

    public void doAction(){
        strategy.doAction();
    }
}

//使用
//IStrategy checkAnswer = new CheckAnswer();
//IStrategy searchHelp = new SearchHelp();
//IStrategy consultTeacher = new ConsultTeacher();
//
//Context context = new Context();
//context.setStrategy(checkAnswer);
//ontext.doAction();
//context.setStrategy(searchHelp);
//context.doAction();
//context.setStrategy(consultTeacher);
//context.doAction();

