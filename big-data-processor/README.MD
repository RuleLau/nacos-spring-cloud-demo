1. 异步获取数据，多线程批量插入到数据库中
2. 异步线程，每次去拿 100 条数据处理，每个 taskNo 只能由一个线程去执行
3. 每个 task 最后会去更新 task 表， task detail 表
4. 分为快任务线程池和慢任务线程池
> https://www.cnblogs.com/chenqingbin/p/15188698.html
    