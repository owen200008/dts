FROM registry.cn-hangzhou.aliyuncs.com/mt_common/get-nacos-cfg:v20210201
ADD dts-web/target/dts.jar  /opt/
EXPOSE 8012