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

  handleSubmit = e => {
    let { form, handleOk, id } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        let { region, } = values;

        handleOk({
          id, region
        });
      }
    });
  };


  render() {
    let { handleCancel, form, region, } = this.props;



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
        maskClosable={false}
      >
        <Form onSubmit={this.handleSubmit} className="login-form">

          <FormItem label="region" {...formItemLayout}>
            {getFieldDecorator("region", {
              rules: [{ required: true, message: "请输入Region" }],
              initialValue: region,
            })(
              <Input placeholder="请输入Region" />
            )}
          </FormItem>

        </Form>
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
