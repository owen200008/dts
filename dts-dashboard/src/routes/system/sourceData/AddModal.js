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
  constructor(props) {
    super(props);
    this.state = {
      dataList: []
    };
    const { dispatch } = props;
    dispatch({
      type: 'sourceData/fetchList',
      payload: {
        pageNum: 1,
        pageSize: 10000
      },
      callback: (data) => {
        this.setState({
          dataList: data,
        })
      }
    })
  }

  handleSubmit = e => {
    const { form, handleOk, id = "" } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        let { name, namespace, table, type, linkSources } = values;
        handleOk({ id, name, namespace, table, type, property: JSON.stringify({ linkSources }) });
      }
    });
  };

  filterOption = (input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0

  render() {
    let { platform, title, handleCancel, form, name, namespace, table, type, property } = this.props;
    let { dataList } = this.state;
    let disable = false;
    const { linkSources } = property ? JSON.parse(property) : {};
    const { getFieldDecorator } = form;

    const formItemLayout = {
      labelCol: {
        sm: { span: 5 }
      },
      wrapperCol: {
        sm: { span: 19 }
      }
    };
    let {
      entityType
    } = platform;





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
          <FormItem label="类型" {...formItemLayout}>
            {getFieldDecorator("type", {
              rules: [{ required: true, message: "请选择类型" }],
              initialValue: type || ''
            })(
              <Select
                placeholder="请选择类型"
              >
                {entityType.map((item, index) => {
                  return (
                    <Option key={index} value={item.code}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
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
          <FormItem label="Link" {...formItemLayout}>
            {getFieldDecorator("linkSources", {
              initialValue: linkSources || []
            })(
              <Select
                mode="multiple"
                placeholder="可多选"
                showSearch={true}
                filterOption={this.filterOption}
              >
                {dataList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
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
