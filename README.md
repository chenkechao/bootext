TBschedule--分布式任务调度框架

1.mven结构搭建
    建bootext/bootext目录，导入bootext/bootext项目到intellij，点击项目上右键 Add Framework Support，选择maven
  把bootext变成module聚合工程，再建bootext-parent父工程
    同理建bootext-mybatis-rw目录，再变成module聚合工程
    在bootext-mybatis-rw建立parent目录，该module的parent为bootext里面的parent目录，这个可以继承总parent里面的配置
    在bootext-mybatis-rw建立starter目录，该module的parent为该工程里面的parent目录
  这样好处，每个模快一个聚合工程单独构建，互相之间独立比属于一个聚合工程更模块化
