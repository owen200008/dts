import React, { PureComponent, Fragment } from "react";
import { Modal, Form, Switch, Input, Select, Divider } from "antd";
import { connect } from "dva";

const { Option } = Select;
const { TextArea } = Input;
const FormItem = Form.Item;

@connect(({ pipeline }) => ({
  pipelineList: pipeline.dataList
}))
class AddModal extends PureComponent {
  constructor(props) {
    super(props);
    const { dispatch, pipelineId } = this.props;

    if (!pipelineId) {
      dispatch({
        type: "pipeline/fetch",
        payload: {
          pageNum: 1,
          pageSize: 10000
        }
      });
    }
  }

  handleSubmit = e => {
    let { form, handleOk, pipelineId } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        let { syncRuleType, namespace, table, startGtid } = values;

        pipelineId = pipelineId || values.pipelineId;
        handleOk({ pipelineId, syncRuleType, namespace, table, startGtid });
      }
    });
  };
  
  filterOption = (input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0

  render() {
    let { pipelineList, handleCancel, form, pipelineId, syncRuleType, namespace, table, startGtid } = this.props;

    let disable = !!pipelineId;


    const { getFieldDecorator } = form;

    const formItemLayout = {
      labelCol: {
        sm: { span: 6 }
      },
      wrapperCol: {
        sm: { span: 18 }
      }
    };





    return (
      <Modal
        width={520}
        centered
        title="同步规则"
        visible
        okText="确定"
        cancelText="取消"
        onOk={this.handleSubmit}
        onCancel={handleCancel}
      >
        <Form onSubmit={this.handleSubmit} className="login-form">
          <FormItem label="通道" {...formItemLayout}>
            {getFieldDecorator("pipelineId", {
              rules: [{ required: true, message: "请选择通道" }],
              initialValue: pipelineId
            })(
              <Select allowClear={true} showSearch={true} filterOption={this.filterOption} disabled={disable}>
                {pipelineList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>
          <FormItem label="同步规则类型" {...formItemLayout}>
            {getFieldDecorator("syncRuleType", {
              rules: [{ required: true, message: "请选择同步规则类型" }],
              initialValue: syncRuleType || 'DEFAULT'
            })(
              <Select allowClear={true} showSearch={true} filterOption={this.filterOption}>
                <Option value='DEFAULT'>DEFAULT</Option>
              </Select>
            )}
          </FormItem>
          <FormItem label="数据库名	" {...formItemLayout}>
            {getFieldDecorator("namespace", {
              rules: [{ required: true, message: "请输入数据库名" }],
              initialValue: namespace,
            })(
              <Input placeholder="请输入数据库名" />
            )}
          </FormItem>
          <FormItem label="数据表名" {...formItemLayout}>
            {getFieldDecorator("table", {
              rules: [{ required: true, message: "请输入数据表名" }],
              initialValue: table,
            })(
              <Input placeholder="请输入数据表名" />
            )}
          </FormItem>
          <FormItem label="同步位置标识" {...formItemLayout}>
            {getFieldDecorator("startGtid", {
              rules: [{ required: true, message: "请输入同步位置标识" }],
              initialValue: startGtid,
            })(
              <Input placeholder="请输入同步位置标识" />
            )}
          </FormItem>
        </Form>
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
