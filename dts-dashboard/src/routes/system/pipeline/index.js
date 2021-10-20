import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Select } from "antd";
import { connect } from "dva";
import { parse } from "qs";
import AddModal from "./AddModal";
import AddRegionModal from "../region/AddModal";
import AddPairModal from "../pair/AddModal";
import AddSyncModal from "../sync/AddModal";

const { Option } = Select;

@connect(({ pipeline, task, dataSource, loading }) => ({
  pipeline,
  task, dataSource,
  loading: loading.effects["task/fetch"] || loading.effects["dataSource/fetch"] || loading.effects["pipeline/fetch"],
}))
export default class Pipeline extends PureComponent {
  constructor(props) {
    super(props);
    let { id: taskId = '' } = parse(props.location.search.substring(1)) || {};
    this.state = {
      taskId,
      currentPage: 1,
      selectedRowKeys: [],
      popup: ""
    };
    this.pageSize = 10;
  }

  componentWillMount() {
    const { dispatch } = this.props;
    const { taskId } = this.state;
    dispatch({
      type: "task/fetch",
      payload: {
        pageNum: 1,
        pageSize: 10000
      }
    });
    if (taskId) {
      this.listItems();
    }
  }

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  listItems = () => {
    const { dispatch } = this.props;
    const { taskId } = this.state;
    dispatch({
      type: "pipeline/fetch",
      payload: {
        taskId
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
    const { currentPage, name, type } = this.state;

    this.setState({
      popup: (
        <AddModal
          disabled={true}
          {...item}
          handleOk={values => {
            dispatch({
              type: "pipeline/update",
              payload: {
                ...values
              },
              fetchValue: {
                type,
                name,
                pageNum: currentPage,
                pageSize: 12
              },
              callback: () => {
                this.setState({ selectedRowKeys: [] });
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

  searchOnSelectchange = (key, value) => {
    this.setState({ [key]: value });
  };

  searchClick = () => {
    this.listItems(1);
    this.setState({ currentPage: 1 });
  };

  deleteClick = (pipeline) => {
    const { dispatch } = this.props;
    const { name, type, currentPage } = this.state;
    dispatch({
      type: "pipeline/delete",
      payload: {
        id: pipeline.id
      },
      fetchValue: {
        name, type,
        pageNum: currentPage,
        pageSize: this.pageSize
      },
      callback: () => {
        this.setState({ selectedRowKeys: [] });
      }
    });
  };

  addClick = () => {
    const { currentPage, name, type, taskId } = this.state;
    this.setState({
      popup: (
        <AddModal
          taskId={taskId}
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "pipeline/add",
              payload: {
                ...values
              },
              fetchValue: {
                name, type,
                pageNum: currentPage,
                pageSize: this.pageSize
              },
              callback: () => {
                this.closeModal();
                /* dispatch({
                  type: "global/fetchPlugins",
                  payload: {
                    callback: () => {
                    }
                  }
                }); */
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

  addRegion = (item) => {
    // const { currentPage, name, type } = this.state;
    this.setState({
      popup: (
        <AddRegionModal
          pipelineId={item.id}
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "region/add",
              payload: {
                ...values
              },
              /* fetchValue: {
                name, type,
                pageNum: currentPage,
                pageSize: this.pageSize
              }, */
              callback: () => {
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

  addPair = (item) => {
    // const { currentPage, name, type } = this.state;
    this.setState({
      popup: (
        <AddPairModal
          pipelineId={item.id}
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "pair/add",
              payload: {
                ...values
              },
              /* 
                            fetchValue: {
                              name, type,
                              pageNum: currentPage,
                              pageSize: this.pageSize
                            },
               */
              callback: () => {
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

  addSync = (item) => {
    // const { currentPage, name, type } = this.state;
    this.setState({
      popup: (
        <AddSyncModal
          pipelineId={item.id}
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "sync/add",
              payload: {
                ...values
              },
              /* 
                            fetchValue: {
                              name, type,
                              pageNum: currentPage,
                              pageSize: this.pageSize
                            },
               */
              callback: () => {
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


  onTableExpand = (expanded, record) => {
    console.info(record);
  }

  expandedRowRender = record => <p>{record.description}</p>


  render() {

    const { pipeline, task, dataSource, loading } = this.props;
    const { dataList } = pipeline;
    let { taskId, popup } = this.state;
    const taskList = task.dataList;
    const dataSourceList = dataSource.dataList;

    const tableColumns = [
      {
        align: "center",
        title: "名称",
        dataIndex: "name",
        key: "name",
      },
      {
        align: "center",
        title: "参数",
        dataIndex: "pipelineParams",
        key: "pipelineParams",
      },
      {
        align: "center",
        title: "操作",
        dataIndex: "time",
        key: "time",
        width: 150,
        render: (text, record) => {
          return (
            <div>
              <Icon
                title='添加region'
                type="appstore"
                style={{ color: 'orange' }}
                onClick={() => {
                  this.addRegion(record);
                }}
              />
              <Icon
                title='添加数据映射'
                type="swap"
                style={{ marginLeft: 20, color: 'blue' }}
                onClick={() => {
                  this.addPair(record);
                }}
              />
              <Icon
                title='添加数据映射'
                type="profile"
                style={{ marginLeft: 20, color: 'green' }}
                onClick={() => {
                  this.addSync(record);
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
            </div>
          );
        }
      }
    ];

    taskId = taskId || '';

    return (
      <div className="plug-content-wrap">

        <Row gutter={24}>
          <Col span={24}>
            <div className="table-header" style={{ paddingTop: 5 }}>
              <Breadcrumb>
                <Breadcrumb.Item><a href="#/home">Home</a></Breadcrumb.Item>
                <Breadcrumb.Item>通道列表</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="table-header" style={{ justifyContent: "normal" }}>
              <Select
                defaultValue={`${taskId}`}
                style={{ width: 150 }}
                onChange={this.searchOnSelectchange.bind(this, 'taskId')}
                placeholder="请选择任务"
              >
                {taskList.map((item, index) => {
                  return (
                    <Option key={index} value={`${item.id}`}>
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

              {taskId ? (
                <Button
                  style={{ marginLeft: 20, marginTop: 0 }}
                  type="primary"
                  onClick={this.addClick}
                >
                  添加
                </Button>
              )
                : null
              }
            </div>
            <Table
              size="small"
              style={{ marginTop: 30 }}
              bordered
              loading={loading}
              columns={tableColumns}
              dataSource={dataList}
              pagination={false}
              onExpand={this.onTableExpand}
              expandedRowRender={this.expandedRowRender}
            />
            {popup}
          </Col>

        </Row>
      </div>
    )
      ;
  }
}
