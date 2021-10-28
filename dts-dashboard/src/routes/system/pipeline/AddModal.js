import React, { PureComponent, Fragment } from "react";
import { Modal, Form, Switch, Input, Select, Divider, Button } from "antd";
import { connect } from "dva";

import ace from 'ace-builds'
import 'ace-builds/webpack-resolver'
import 'ace-builds/src-noconflict/theme-xcode' // 默认设置的主题
import 'ace-builds/src-noconflict/mode-json' // 默认设置的语言模式
import { formatJSON } from "../../../utils/jsonUtil";
import AddDataSourceModal from '../dataSource/AddModal';

const { Option } = Select;
const { TextArea } = Input;
const FormItem = Form.Item;

@connect(({ global, task, dataSource }) => ({
  platform: global.platform,
  taskList: task.dataList,
  dataSourceList: dataSource.dataList
}))
class AddModal extends PureComponent {
  constructor(props) {
    super(props);
    const { sourceEntityId, targetEntityId } = props;
    this.state = {
      popup: null,
      sourceEntityId,
      targetEntityId
    }
  }

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({
      type: "dataSource/fetch",
      payload: {
        pageNum: 1,
        pageSize: 10000
      }
    });
    setTimeout(this.initAceEditor, 100);
  }

  componentWillUnmount() {
    this.destroyEditor();
  }

  handleSubmit = e => {
    const { form, handleOk, id = "" } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        let pipelineParams = this.jsonEncode(this.configEditor);
        let { name, taskId, sourceEntityId, targetEntityId } = values;
        try {
          pipelineParams && JSON.parse(pipelineParams);
        } catch (ex) {
          return alert('请输入正确配置');
        }

        handleOk({ id, name, taskId, sourceEntityId, targetEntityId, pipelineParams });
      }
    });
  };

  initAceEditor = () => {
    this.destroyEditor();
    this.editor = ace.edit(this.refs.editor);
    this.editor.setTheme('ace/theme/xcode');
    let JsMode = ace.require('ace/mode/json').Mode;
    this.editor.session.setMode(new JsMode());
  }

  destroyEditor = () => {
    if (this.editor) {
      this.editor.destroy();
      this.editor.container.remove();
      this.editor = null;
    }
  }

  jsonEncode = () => {
    let jsonDoc = this.editor.getValue();
    jsonDoc = formatJSON(jsonDoc, true);
    return jsonDoc;
  }


  closeModal = () => {
    this.setState({ popup: null });
  };

  filterOption = (input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0

  addDataSource = (key) => {
    const { dispatch } = this.props;
    this.setState({
      popup: (
        <AddDataSourceModal
          disabled={false}
          handleOk={values => {
            dispatch({
              type: "dataSource/add",
              payload: {
                ...values
              },
              fetchValue: {
                pageNum: 1,
                pageSize: 10000
              },
              callback: ({ entityId }) => {
                this.setState({
                  [key]: entityId
                })
                this.closeModal();
              }
            });
          }}
          handleCancel={() => {
            this.closeModal();
          }}
        />
      )
    });
  }

  render() {
    let { taskList, dataSourceList, handleCancel, form, name, taskId, pipelineParams } = this.props;
    let { sourceEntityId, targetEntityId, popup } = this.state;


    const { getFieldDecorator } = form;

    const formItemLayout = {
      labelCol: {
        sm: { span: 5 }
      },
      wrapperCol: {
        sm: { span: 19 }
      }
    };

    // const srcList = dataSourceList.filter(item => !targetEntityId || item.id !== targetEntityId);
    // const targetList = dataSourceList.filter(item => !sourceEntityId || item.id !== sourceEntityId);
    return (
      <Modal
        width={520}
        centered
        title="通道"
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
              <Input placeholder="请输入名称" />
            )}
          </FormItem>
          <FormItem label="任务" {...formItemLayout}>
            {getFieldDecorator("taskId", {
              rules: [{ required: true, message: "请选择任务" }],
              initialValue: taskId || ''
            })(
              <Select allowClear={true} showSearch={true} filterOption={this.filterOption}>
                {taskList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
          </FormItem>
          <FormItem label="同步源" {...formItemLayout}>
            {getFieldDecorator("sourceEntityId", {
              rules: [{ required: true, message: "请选择同步源" }],
              initialValue: sourceEntityId
            })(
              <Select style={{ width: '70%' }} allowClear={true} showSearch={true} filterOption={this.filterOption}>
                {dataSourceList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {`${item.name} -- ${item.type}`}
                    </Option>
                  );
                })}
              </Select>
            )}
            <Button
              style={{ marginLeft: 20, marginTop: 0 }}
              onClick={this.addDataSource.bind(this, 'sourceEntityId')}
              icon="plus"
            >
              添加
            </Button>
          </FormItem>
          <FormItem label="同步目标" {...formItemLayout}>
            {getFieldDecorator("targetEntityId", {
              rules: [{ required: true, message: "请选择同步目标" }],
              initialValue: targetEntityId// pluginTypeEnums[typePlugin].name
            })(
              <Select style={{ width: '70%' }} allowClear={true} showSearch={true} filterOption={this.filterOption}>
                {dataSourceList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {`${item.name} -- ${item.type}`}
                    </Option>
                  );
                })}
              </Select>
            )}
            <Button
              style={{ marginLeft: 20, marginTop: 0 }}
              onClick={this.addDataSource.bind(this, 'targetEntityId')}
              icon="plus"
            >
              添加
            </Button>
          </FormItem>
          <FormItem label="参数" {...formItemLayout}>
            <div style={{ width: '100%', height: '200px' }} ref="editor">{formatJSON(pipelineParams)}</div>
          </FormItem>
          {/* <FormItem label="类型" {...formItemLayout}>
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
        {popup}
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
