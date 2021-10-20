import React, { PureComponent, Fragment } from "react";
import { Modal, Form, Switch, Input, Select, Divider } from "antd";
import { connect } from "dva";

const { Option } = Select;
const { TextArea } = Input;
const FormItem = Form.Item;

@connect(({ global }) => ({
  platform: global.platform
}))
class AddModal extends PureComponent {
  handleSubmit = e => {
    const { form, handleOk, id = "" } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        let { name, type, encode, slaveId, url, driver, username, password, mysql } = values;


        handleOk({ id, name, type, encode, slaveId, url, driver, username, password, mysql });
      }
    });
  };

  render() {
    let { handleCancel, form, id, name, /* type, encode, slaveId, url, driver, username, password, mysql */ } = this.props;

    let disable = false;
    if (id) {
      disable = true;
    }

    const { getFieldDecorator } = form;

    const formItemLayout = {
      labelCol: {
        sm: { span: 5 }
      },
      wrapperCol: {
        sm: { span: 19 }
      }
    };
    /*  let {
       pluginTypeEnums
     } = platform; */





    return (
      <Modal
        width={520}
        centered
        title="任务"
        visible
        okText="确定"
        cancelText="取消"
        onOk={this.handleSubmit}
        onCancel={handleCancel}
      >
        <Form onSubmit={this.handleSubmit} className="login-form">
          <FormItem label="名称" {...formItemLayout}>
            {getFieldDecorator("name", {
              rules: [{ required: true, message: "请输入任务名称" }],
              initialValue: name,
            })(
              <Input placeholder="请输入任务名称" disabled={disable} />
            )}
          </FormItem>
          {/* <FormItem label="数据类型" {...formItemLayout}>
            {getFieldDecorator("type", {
              rules: [{ required: true, message: "请输入数据类型" }],
              initialValue: type,
            })(
              <Input placeholder="请输入数据类型" disabled={disable} />
            )}
          </FormItem>
          <FormItem label="数据编码格式" {...formItemLayout}>
            {getFieldDecorator("encode", {
              rules: [{ required: false, message: "请输入数据编码格式" }],
              initialValue: encode,
            })(
              <Input placeholder="请输入数据编码格式" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="组类型	" {...formItemLayout}>
            {getFieldDecorator("slaveId", {
              rules: [{ required: true, message: "请输入组类型" }],
              initialValue: slaveId,
            })(
              <Input placeholder="请输入组类型" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="JDBC URL" {...formItemLayout}>
            {getFieldDecorator("url", {
              rules: [{ required: true, message: "请输入jdbc url" }],
              initialValue: url,
            })(
              <Input placeholder="请输入jdbc url" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="驱动" {...formItemLayout}>
            {getFieldDecorator("driver", {
              rules: [{ required: true, message: "请输入驱动" }],
              initialValue: driver,
            })(
              <Input placeholder="请输入驱动" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="账号" {...formItemLayout}>
            {getFieldDecorator("username", {
              rules: [{ required: true, message: "请输入账号" }],
              initialValue: username,
            })(
              <Input placeholder="请输入账号" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="密码" {...formItemLayout}>
            {getFieldDecorator("password", {
              rules: [{ required: true, message: "请输入密码" }],
              initialValue: password,
            })(
              <Input placeholder="请输入密码" disabled={disable}  />
            )}
          </FormItem>
          <FormItem label="mysql" {...formItemLayout}>
            {getFieldDecorator("mysql", {
              rules: [{ required: false, message: "请输入mysql" }],
              initialValue: mysql,
            })(
              <Input placeholder="请输入mysql" disabled={disable}  />
            )}
          </FormItem>
           <FormItem label="类型" {...formItemLayout}>
            {getFieldDecorator("typePlugin", {
              rules: [{required: true, message: "请选择类型"}],
              initialValue: typePlugin// pluginTypeEnums[typePlugin].name
            })(
              <Select>
                {pluginTypeEnums.map((item, index) => {
                  return (
                    <Option key={index} value={item.code}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>
          <FormItem {...formItemLayout} label="是否开启">
            {getFieldDecorator("isValid", {
              initialValue: isValid === 1,
              valuePropName: "checked"
            })(<Switch/>)}
          </FormItem> */}
        </Form>
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
