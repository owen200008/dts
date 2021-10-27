import React, { PureComponent } from "react";
import { Table, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Select } from "antd";
import { connect } from "dva";
import { parse } from "qs";
import AddModal from "./AddModal";

const { Option } = Select;
@connect(({ pair, pipeline, loading }) => ({
  pair,
  pipeline,
  loading: loading.effects["pair/fetch"],
}))
export default class Pair extends PureComponent {
  constructor(props) {
    super(props);
    let { pipelineId = 0, taskId = 0, } = parse(props.location.search.substring(1)) || {};
    this.state = {
      pipelineId: +pipelineId,
      taskId: +taskId,
      currentPage: 1,
      selectedRowKeys: [],
      popup: ""
    };
    this.pageSize = 10;
  }

  componentWillMount() {
    const { currentPage, taskId } = this.state;
    const { dispatch } = this.props;
    dispatch({
      type: "pipeline/fetch",
      payload: {
        taskId: taskId || undefined,
        pageNum: 1,
        pageSize: 10000
      }
    });
    this.listItems(currentPage);
  }

  searchOnSelectchange = (key, value) => {
    this.setState({ [key]: value });
  };

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  listItems = page => {
    const { dispatch } = this.props;
    const { pipelineId } = this.state;
    if (!pipelineId) return;
    dispatch({
      type: "pair/fetch",
      payload: {
        pipelineId: pipelineId || undefined,
        pageNum: page,
        pageSize: this.pageSize
      }
    });
  };

  pageOnchange = page => {
    this.setState({ currentPage: page });
    this.listItems(page);
  };

  closeModal = () => {
    this.setState({ popup: "" });
  };

  editClick = item => {
    const { dispatch } = this.props;
    const { currentPage } = this.state;

    this.setState({
      popup: (
        <AddModal
          disabled={true}
          {...item}
          handleOk={values => {
            dispatch({
              type: "pair/update",
              payload: {
                ...values
              },
              callback: () => {
                this.setState({ pipelineId: values.pipelineId, selectedRowKeys: [] }, () => {
                  if (values.pipelineId === item.pipelineId) {
                    this.pageOnchange(currentPage);
                  } else {
                    this.searchClick();
                  }
                });
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
  };

  searchOnchange = (key, e) => {
    const value = e.target.value;
    this.setState({ [key]: value });
  };

  searchClick = () => {
    this.listItems(1);
    this.setState({ currentPage: 1 });
  };

  deleteClick = (pair) => {
    const { dispatch } = this.props;
    const { currentPage, pipelineId } = this.state;
    dispatch({
      type: "pair/delete",
      payload: {
        pairId: pair.id
      },
      fetchValue: {
        pipelineId: pipelineId || undefined,
        pageNum: currentPage,
        pageSize: this.pageSize
      },
      callback: () => {
        this.setState({ selectedRowKeys: [] });
      }
    });
  };

  addClick = () => {
    const { pipelineId } = this.state;
    this.setState({
      popup: (
        <AddModal
          pipelineId={pipelineId}
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "pair/add",
              payload: {
                ...values
              },
              callback: () => {
                this.closeModal();
                this.setState({ pipelineId: values.pipelineId, selectedRowKeys: [] }, this.searchClick);
              }
            });
          }}
          handleCancel={() => {
            this.closeModal();
          }}
        />
      )
    });
  };






  render() {

    const { pair, loading, pipeline } = this.props;
    const { dataList, total } = pair;
    const { dataList: pipelineList } = pipeline;
    const { currentPage, popup, pipelineId } = this.state;

    const tableColumns = [
      {
        align: "center",
        title: "通道名称",
        dataIndex: "pipelineName",
        key: "pipelineName",
      },
      {
        align: "center",
        title: "源数据",
        dataIndex: "sourceData",
        key: "sourceData",
      },
      {
        align: "center",
        title: "目标数据",
        dataIndex: "targetData",
        key: "targetData",
      },


      {
        align: "center",
        title: "操作",
        dataIndex: "time",
        key: "time",
        width: 120,
        render: (text, record) => {
          return (
            <div>
              <Icon
                title='编辑'
                type="edit"
                style={{ color: 'orange' }}
                onClick={() => {
                  this.editClick(record);
                }}
              />
              <Popconfirm
                title="你确认删除吗"
                placement='bottom'
                onConfirm={() => {
                  this.deleteClick(record)
                }}
                okText="确认"
                cancelText="取消"
              >
                <Icon title='删除' type="delete" style={{ marginLeft: 20, color: 'red' }} />
              </Popconfirm>
              {/* <Icon
                title='查看配置'
                type="dash"
                style={{ marginLeft: 20, color: 'blue' }}
                onClick={this.viewPluginConfig.bind(this, record)}
              /> */}
            </div>
          );
        }
      }
    ];



    return (
      <div className="plug-content-wrap">

        <Row gutter={24}>
          <Col span={24}>
            <div className="table-header" style={{ paddingTop: 5 }}>
              <Breadcrumb>
                <Breadcrumb.Item><a href="#/home">Home</a></Breadcrumb.Item>
                <Breadcrumb.Item>数据映射列表</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="table-header" style={{ justifyContent: "normal" }}>
              <Select
                value={pipelineId || ''}
                style={{ width: 150 }}
                onChange={this.searchOnSelectchange.bind(this, 'pipelineId')}
                placeholder="请选择通道"
              >
                {pipelineList.map((item, index) => {
                  return (
                    <Option key={index} value={item.id}>
                      {item.name}
                    </Option>
                  );
                })}
              </Select>
              <Button
                type="primary"
                style={{ marginLeft: 20, marginTop: 0 }}
                onClick={this.searchClick}
              >
                查询
              </Button>


              <Button
                style={{ marginLeft: 20, marginTop: 0 }}
                type="primary"
                onClick={this.addClick}
              >
                添加
              </Button>
            </div>
            <Table
              size="small"
              style={{ marginTop: 30 }}
              bordered
              loading={loading}
              columns={tableColumns}
              dataSource={dataList}
              pagination={{
                total,
                current: currentPage,
                pageSize: this.pageSize,
                onChange: this.pageOnchange
              }}
            />
            {popup}
          </Col>

        </Row>
      </div>
    );
  }
}
