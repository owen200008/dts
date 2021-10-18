set curdir=%~dp0
@echo ����Ŀ¼��%curdir%
cd %curdir%
cd ../../..
call mvn org.mybatis.generator:mybatis-generator-maven-plugin:1.3.5:generate
pause