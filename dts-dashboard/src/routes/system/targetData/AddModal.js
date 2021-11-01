import React, { PureComponent, Fragment } from "react";
import { Modal, Form, Switch, Input, Select, Divider } from "antd";
import { connect } from "dva";

const { Option } = Select;
const { TextArea } = Input;
const FormItem = Form.Item;

@connect(({ global }) => ({
  platform: global.platform,
}))
class AddModal extends PureComponent {
  handleSubmit = e => {
    const { form, handleOk, id = "" } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        let { name, namespace, table, suncRule } = values;


        handleOk({ id, name, namespace, table, suncRule });
      }
    });
  };

  render() {
    let { platform, title, handleCancel, form, name, namespace, table, suncRule } = this.props;
    let {
      syncRule: syncRuleList,
    } = platform;
    let disable = false;

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
        title={`添加${title}`}
        visible
        okText="确定"
        cancelText="取消"
        onOk={this.handleSubmit}
        onCancel={handleCancel}
        maskClosable={false}
      >
        <Form onSubmit={this.handleSubmit} className="login-form">
          <FormItem label="名称" {...formItemLayout}>
            {getFieldDecorator("name", {
              rules: [{ required: true, message: "请输入名称" }],
              initialValue: name,
            })(
              <Input placeholder="请输入名称" disabled={disable} />
            )}
          </FormItem>
          <FormItem label="数据库名" {...formItemLayout}>
            {getFieldDecorator("namespace", {
              rules: [{ required: true, message: "请输入数据库名" }],
              initialValue: namespace,
            })(
              <Input placeholder="请输入数据库名" disabled={disable} />
            )}
          </FormItem>
          <FormItem label="数据表名		" {...formItemLayout}>
            {getFieldDecorator("table", {
              rules: [{ required: true, message: "请输入数据表名" }],
              initialValue: table,
            })(
              <Input placeholder="请输入数据表名" disabled={disable} />
            )}
          </FormItem>

          <FormItem label="同步规则" {...formItemLayout}>
            {getFieldDecorator("suncRule", {
              rules: [{ required: true, message: "请选择同步规则" }],
              initialValue: suncRule || ''
            })(
              <Select>
                {syncRuleList.map((item, index) => {
                  return (
                    <Option key={index} value={item.code}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>

        </Form>
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
