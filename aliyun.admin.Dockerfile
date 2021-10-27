FROM registry.cn-hangzhou.aliyuncs.com/mt_common/get-nacos-cfg:v20210201
ADD dts-admin/target/dts-admin.jar /opt
EXPOSE 8011