import React, { PureComponent, Fragment } from "react";
import { Modal, Form, Switch, Input, Select, Divider, Button } from "antd";
import { connect } from "dva";

import AddDataMediaModal from '../sourceData/AddModal';
import AddTargetDataMediaModal from '../targetData/AddModal';

const { Option } = Select;
const FormItem = Form.Item;

const labelMaps = {
  sourceDatamediaId: '源数据',
  targetDatamediaId: '目标数据'
}


@connect(({ global, pipeline, sourceData, targetData }) => ({
  platform: global.platform,
  pipelineList: pipeline.dataList,
  sourceDataList: sourceData.dataList,
  targetDataList: targetData.dataList,
}))
class AddModal extends PureComponent {
  constructor(props) {
    super(props);
    const { sourceDatamediaId, targetDatamediaId, dispatch } = props;
    this.state = {
      popup: null,
      sourceDatamediaId,
      targetDatamediaId
    }

    dispatch({
      type: "sourceData/fetch",
      payload: {
        pageNum: 1,
        pageSize: 10000
      }
    });
    dispatch({
      type: "targetData/fetch",
      payload: {
        pageNum: 1,
        pageSize: 10000
      }
    });
  }


  handleSubmit = e => {
    let { form, handleOk, id } = this.props;
    e.preventDefault();
    form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        let { sourceDatamediaId, targetDatamediaId, pipelineId } = values;

        handleOk({ pipelineId, sourceDatamediaId, targetDatamediaId, id });
      }
    });
  };


  closeModal = () => {
    this.setState({ popup: null });
  };

  filterOption = (input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0

  addDataSource = (key) => {
    const { dispatch } = this.props;
    this.setState({
      popup: (
        <AddDataMediaModal
          disabled={false}
          title={labelMaps[key]}
          handleOk={values => {
            dispatch({
              type: `${key.replace('mediaId', '')}/add`,
              payload: {
                ...values
              },
              fetchValue: {
                pageNum: 1,
                pageSize: 10000
              },
              callback: ({ sourceDataId, targetDataId }) => {
                this.setState({
                  [key]: sourceDataId || targetDataId
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

  addTargetData = (key) => {
    const { dispatch } = this.props;
    this.setState({
      popup: (
        <AddTargetDataMediaModal
          disabled={false}
          title={labelMaps[key]}
          handleOk={values => {
            dispatch({
              type: `${key.replace('mediaId', '')}/add`,
              payload: {
                ...values
              },
              fetchValue: {
                pageNum: 1,
                pageSize: 10000
              },
              callback: ({ sourceDataId, targetDataId }) => {
                this.setState({
                  [key]: sourceDataId || targetDataId
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
    let { pipelineList, sourceDataList, targetDataList, handleCancel, form, pipelineId } = this.props;
    let { sourceDatamediaId, targetDatamediaId, popup } = this.state;

    const { getFieldDecorator } = form;

    const formItemLayout = {
      labelCol: {
        sm: { span: 5 }
      },
      wrapperCol: {
        sm: { span: 19 }
      }
    };

    return (
      <Modal
        width={520}
        centered
        title="通道与数据映射"
        visible
        okText="确定"
        cancelText="取消"
        onOk={this.handleSubmit}
        onCancel={handleCancel}
        maskClosable={false}
      >
        <Form onSubmit={this.handleSubmit} className="login-form">
          <FormItem label="通道" {...formItemLayout}>
            {getFieldDecorator("pipelineId", {
              rules: [{ required: true, message: "请选择通道" }],
              initialValue: pipelineId || ''
            })(
              <Select allowClear={true} showSearch={true} filterOption={this.filterOption}>
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
          <FormItem label={labelMaps.sourceDatamediaId} {...formItemLayout}>
            {getFieldDecorator("sourceDatamediaId", {
              rules: [{ required: true, message: `请选择${labelMaps.sourceDatamediaId}` }],
              initialValue: sourceDatamediaId
            })(
              <Select style={{ width: '70%' }} allowClear={true} showSearch={true} filterOption={this.filterOption}>
                {sourceDataList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
            <Button
              style={{ marginLeft: 20, marginTop: 0 }}
              onClick={this.addDataSource.bind(this, 'sourceDatamediaId')}
              icon="plus"
            >
              添加
            </Button>
          </FormItem>
          <FormItem label={labelMaps.targetDatamediaId} {...formItemLayout}>
            {getFieldDecorator("targetDatamediaId", {
              rules: [{ required: true, message: `请选择${labelMaps.targetDatamediaId}` }],
              initialValue: targetDatamediaId// pluginTypeEnums[typePlugin].name
            })(
              <Select style={{ width: '70%' }} allowClear={true} showSearch={true} filterOption={this.filterOption}>
                {targetDataList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
            )}
            <Button
              style={{ marginLeft: 20, marginTop: 0 }}
              onClick={this.addTargetData.bind(this, 'targetDatamediaId')}
              icon="plus"
            >
              添加
            </Button>
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
