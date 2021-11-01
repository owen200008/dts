/* eslint-disable no-unused-expressions */
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
    const { sourceEntityId, targetEntityId, pipelineParams, dispatch } = props;
    this.state = {
      popup: null,
      sourceEntityId,
      targetEntityId,
      pipelineParams,
    }

    if (pipelineParams === null || pipelineParams === undefined) {

      dispatch({
        type: "pipeline/defaultParams",
        payload: {
          pageNum: 1,
          pageSize: 10000
        },
        callback: (resp) => {
          const val = formatJSON(JSON.stringify(resp));
          if (this.editor) {
            this.editor.setValue(val);
          } else {
            this.setState({ pipelineParams: val });
          }
        }
      })
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
    setTimeout(() => {
      this.initAceEditor();
      // this.initSelectAceEditor();
      // this.initLoadAceEditor();
    }, 100);
  }

  componentWillUnmount() {
    this.destroyEditor();
    // this.destroySelectEditor();
    // this.destroyLoadEditor();
  }

  handleSubmit = e => {
    const { form, handleOk, id = "" } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        let pipelineParams = this.jsonEncode('editor');
        // let selectParam = this.jsonEncode('selectEditor');
        // let loadParam = this.jsonEncode('loadEditor');
        let { name, taskId, sourceEntityId, targetEntityId, dataType, /* fullDistribute, dispatchRuleType */ } = values;
        try {
          pipelineParams && JSON.parse(pipelineParams);
        } catch (ex) {
          return alert('请输入正确通道配置');
        }
        /* try {
          selectParam && JSON.parse(selectParam);
        } catch (ex) {
          return alert('请输入正确Select配置');
        }
        try {
          loadParam && JSON.parse(loadParam);
        } catch (ex) {
          return alert('请输入正确LOad配置');
        } */

        handleOk({ id, name, taskId, sourceEntityId, targetEntityId, pipelineParams, dataType, /* fullDistribute, dispatchRuleType, selectParam, loadParam */ });
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

  /* initSelectAceEditor = () => {
    this.destroySelectEditor();
    this.selectEditor = ace.edit(this.refs.selectEditor);
    this.selectEditor.setTheme('ace/theme/xcode');
    let JsMode = ace.require('ace/mode/json').Mode;
    this.selectEditor.session.setMode(new JsMode());
  }

  initLoadAceEditor = () => {
    this.destroyLoadEditor();
    this.loadEditor = ace.edit(this.refs.loadEditor);
    this.loadEditor.setTheme('ace/theme/xcode');
    let JsMode = ace.require('ace/mode/json').Mode;
    this.loadEditor.session.setMode(new JsMode());
  } */

  destroyEditor = () => {
    if (this.editor) {
      this.editor.destroy();
      this.editor.container.remove();
      this.editor = null;
    }
  }

  /* destroyLoadEditor = () => {
    if (this.loadEditor) {
      this.loadEditor.destroy();
      this.loadEditor.container.remove();
      this.loadEditor = null;
    }
  }

  destroySelectEditor = () => {
    if (this.selectEditor) {
      this.selectEditor.destroy();
      this.selectEditor.container.remove();
      this.selectEditor = null;
    }
  } */

  jsonEncode = (editor = 'editor') => {
    let jsonDoc = this[editor].getValue();
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
    let { platform, taskList, dataSourceList, handleCancel, form, name, taskId, dataType } = this.props;
    let { sourceEntityId, targetEntityId, popup, pipelineParams } = this.state;

    const { entityType = [] } = platform;
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
          <FormItem label="数据类型" {...formItemLayout}>
            {getFieldDecorator("dataType", {
              rules: [{ required: true, message: "请选择数据类型" }],
              initialValue: dataType,
            })(
              <Select>
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
          <FormItem label="通道参数" {...formItemLayout}>
            <div style={{ width: '100%', height: '300px' }} ref="editor">{formatJSON(pipelineParams)}</div>
          </FormItem>
          {/* <FormItem {...formItemLayout} label="全量分发">
            {getFieldDecorator("fullDistribute", {
              initialValue: fullDistribute,
              rules: [{ required: true, }],
              valuePropName: "checked"
            })(<Switch />)}
          </FormItem>
          <FormItem {...formItemLayout} label="分发规则	">
            {getFieldDecorator("dispatchRuleType", {
              rules: [{ required: true, message: "请选择分发规则" }],
              initialValue: dispatchRuleType
            })(
              <Select>
                {ruleList.map((item, index) => {
                  return (
                    <Option key={index} value={item}>
                      {item}
                    </Option>
                  );
                })}
              </Select>
            )}
              </FormItem> 
          <FormItem label="Select参数" {...formItemLayout}>
            <div style={{ width: '100%', height: '100px' }} ref="selectEditor">{formatJSON(selectParam)}</div>
          </FormItem>
          <FormItem label="Load参数" {...formItemLayout}>
            <div style={{ width: '100%', height: '100px' }} ref="loadEditor">{formatJSON(loadParam)}</div>
          </FormItem> */}
        </Form>
        {popup}
      </Modal>
    );
  }
}

export default Form.create()(AddModal);
