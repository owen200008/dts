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

        let { sourceRegion, targetRegion } = values;

        pipelineId = pipelineId || values.pipelineId;
        handleOk({ pipelineId, sourceRegion, targetRegion });
      }
    });
  };
  
  filterOption = (input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0

  render() {
    let { pipelineList, handleCancel, form, sourceRegion, targetRegion, pipelineId } = this.props;

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
        title="Region"
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
          <FormItem label="select端Region" {...formItemLayout}>
            {getFieldDecorator("sourceRegion", {
              rules: [{ required: true, message: "请输入select端Region" }],
              initialValue: sourceRegion,
            })(
              <Input placeholder="请输入select端Region" />
            )}
          </FormItem>
          <FormItem label="load端Region" {...formItemLayout}>
            {getFieldDecorator("targetRegion", {
              rules: [{ required: true, message: "请输入load端Region" }],
              initialValue: targetRegion,
            })(
              <Input placeholder="请输入load端Region" />
            )}
          </FormItem>
          {/* <FormItem label="组类型	" {...formItemLayout}>
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
