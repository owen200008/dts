import React, { PureComponent, Fragment } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Select, Descriptions } from "antd";
import { connect } from "dva";
import { parse } from "qs";
import AddModal from './AddModal';

const { Option } = Select;

@connect(({ pipeline, task, dataSource, loading }) => ({
  pipeline,
  task, dataSource,
  loading: loading.effects["task/fetch"] || loading.effects["dataSource/fetch"] || loading.effects["pipeline/fetch"],
}))
export default class Pipeline extends PureComponent {
  constructor(props) {
    super(props);
    let { id = 0 } = parse(props.location.search.substring(1)) || {};
    this.state = {
      taskId: +id,
      currentPage: 1,
      selectedRowKeys: [],
      popup: ""
    };
    this.pageSize = 10;
  }

  componentWillMount() {
    const { dispatch } = this.props;
    dispatch({
      type: 'pipeline/saveList',
      payload: {
        dataList: [],
        total: 0
      }
    })
    dispatch({
      type: "task/fetch",
      payload: {
        pageNum: 1,
        pageSize: 10000
      }
    });
    this.listItems(1);
  }

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  listItems = (pageNum) => {
    const { dispatch } = this.props;
    const { taskId } = this.state;
    if (!taskId) return;
    dispatch({
      type: "pipeline/fetch",
      payload: {
        taskId: taskId || undefined,
        pageNum,
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
              type: "pipeline/update",
              payload: {
                ...values
              },

              callback: () => {
                this.setState({ selectedRowKeys: [], taskId: values.taskId }, () => {
                  if (values.taskId === item.taskId) {
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

  searchOnSelectchange = (key, value) => {
    this.setState({ [key]: value });
  };

  searchClick = () => {
    this.listItems(1);
    this.setState({ currentPage: 1 });
  };

  deleteClick = (pipeline) => {
    const { dispatch } = this.props;
    const { taskId, currentPage } = this.state;
    dispatch({
      type: "pipeline/delete",
      payload: {
        pipelineId: pipeline.id
      },
      fetchValue: {
        taskId,
        pageNum: currentPage,
        pageSize: this.pageSize
      },
      callback: () => {
        this.setState({ selectedRowKeys: [] });
      }
    });
  };

  deleteSyncClick = (item, pipeline) => {
    const { dispatch } = this.props;
    dispatch({
      type: "sync/delete",
      payload: {
        id: item.id
      },
      callback: () => {
        pipeline.extraInfo.sync.splice(pipeline.extraInfo.sync.findIndex(s => s.id === item.id), 1);
        this.updateItem(pipeline);
      }
    });
  };

  addClick = () => {
    const { taskId } = this.state;
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
              callback: () => {
                this.setState({ selectedRowKeys: [], taskId: values.taskId }, this.searchClick);
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

  toRegionNacos = (item) => {
    window.open(`#/system/nacos?regionId=${item.regionId}`, '_blank');
  };

  addRegion = (item) => {
    window.open(`#/system/regionPipe?pipelineId=${item.id}&taskId=${item.taskId}`, '_blank');
  };

  addPair = (item) => {
    window.open(`#/system/pair?pipelineId=${item.id}&taskId=${item.taskId}`, '_blank');
  };

  toSync = (item) => {
    // const { currentPage, name, type } = this.state;
    window.open(`#/system/sync?pipelineId=${item.id}&taskId=${item.taskId}`, '_blank');
  };

  onTableExpand = (expanded, record) => {
    const { loading, id } = record;
    if (!expanded || loading && loading === 1) return;
    let payload = {
      pipelineId: id
    };
    this.updateItem({ id, loading: 1 });
    const { dispatch } = this.props;
    Promise.all([
      new Promise((resolve) => {
        dispatch({
          type: 'sync/fetchById',
          payload,
          callback: resolve
        })
      }),
      new Promise((resolve) => {
        dispatch({
          type: 'regionPipe/fetch',
          payload,
          callback: resolve
        })
      }),
      new Promise((resolve) => {
        dispatch({
          type: 'pair/fetchById',
          payload,
          callback: resolve
        })
      }).then(pairs => {
        if (!pairs?.length) return [];
        return Promise.all(pairs.map(pair => {
          return Promise.all([
            new Promise((resolve) => {
              dispatch({
                type: 'sourceData/fetchItem',
                payload: { sourceId: pair.sourceDatamediaId },
                callback: resolve
              })
            }),
            new Promise((resolve) => {
              dispatch({
                type: 'targetData/fetchItem',
                payload: { targetId: pair.targetDatamediaId },
                callback: resolve
              })
            }),
          ]);
        }));
      }),
      new Promise((resolve) => {
        dispatch({
          type: 'dataSource/fetchItem',
          payload: {
            id: record.sourceEntityId
          },
          callback: resolve
        })
      }),
      new Promise((resolve) => {
        dispatch({
          type: 'dataSource/fetchItem',
          payload: {
            id: record.targetEntityId
          },
          callback: resolve
        })
      })
    ]).then(([sync, region, pairs, sourceEntity, targetEntity]) => {
      this.updateItem({
        id,
        loading: 2,
        extraInfo: {
          sync, region, pairs, sourceEntity, targetEntity
        }
      });
    }).catch(() => {
      this.updateItem({
        id,
        loading: 3
      });
    });
  }

  expandedRowRender = record => {
    const { loading, extraInfo } = record;
    if (loading === 2) {
      const { sync, region, pairs, sourceEntity, targetEntity } = extraInfo;
      let syncList = sync?.map(item => {
        item.key = item.id;
        return item;
      }) || [];
      return (
        <Fragment>
          <Descriptions style={{ marginBottom: 20 }} title="数据源" size="small" column={2}>
            <Descriptions.Item label="同步源">{sourceEntity?.name}</Descriptions.Item>
            <Descriptions.Item label="同步目标">{targetEntity?.name}</Descriptions.Item>
          </Descriptions>
          {region?.length ?
            (
              <Descriptions
                style={{ marginBottom: 20 }}
                title="Region"
                size="small"
                column={2}
              >
                {
                  region.map(item => (
                    <Descriptions.Item label={item.mode} key={item.id}>
                      {item.region}
                      <Icon
                        title='运行实例'
                        type="api"
                        style={{ color: 'orange' }}
                        onClick={this.toRegionNacos.bind(this, item)}
                      />
                    </Descriptions.Item>
                  ))
                }
              </Descriptions>
            )
            : null}
          {pairs?.length ?
            (
              <Descriptions style={{ marginBottom: 20 }} title="数据映射关系" size="small" column={2}>
                {pairs.map(pair => (
                  <React.Fragment key={`${pair.id}_0`}>
                    <Descriptions.Item label="源数据">{pair[0]?.name}</Descriptions.Item>
                    <Descriptions.Item label="目标数据">{pair[1]?.name}</Descriptions.Item>
                  </React.Fragment>
                ))
                }
              </Descriptions>
            ) : null
          }
          {syncList?.length ? (
            <Descriptions title="同步规则" size="small">
              <Descriptions.Item>
                <Table
                  size="small"
                  bordered
                  columns={[{
                    align: "center",
                    title: "同步规则类型",
                    dataIndex: "syncRuleType",
                    key: "syncRuleType",
                  },
                  {
                    align: "center",
                    title: "数据库名",
                    dataIndex: "namespace",
                    key: "namespace",
                  },
                  {
                    align: "center",
                    title: "数据表名",
                    dataIndex: "table",
                    key: "table",
                  },
                  {
                    align: "center",
                    title: "同步位置标识",
                    dataIndex: "startGtid",
                    key: "startGtid",
                  },
                  {
                    align: "center",
                    title: "操作",
                    dataIndex: "id",
                    key: "id",
                    width: 120,
                    render: (text, syncRecord) => {
                      return (
                        <div>
                          <Popconfirm
                            title="你确认删除吗"
                            placement='bottom'
                            onConfirm={() => {
                              this.deleteSyncClick(syncRecord, record)
                            }}
                            okText="确认"
                            cancelText="取消"
                          >
                            <Icon title='删除' type="delete" style={{ marginLeft: 0, color: 'red' }} />
                          </Popconfirm>
                        </div>
                      );
                    }
                  }
                  ]}
                  dataSource={syncList}
                  pagination={false}
                />
              </Descriptions.Item>
            </Descriptions>
          ) : null
          }
        </Fragment>
      );
    } else if (loading === 3) {
      return <p>加载失败，请重试</p>
    }
    return <p>加载中……</p>

  }

  updateItem(payload) {
    const { dispatch } = this.props;
    dispatch({
      type: 'pipeline/updateItem',
      payload,
    })
  }


  render() {

    const { pipeline, task, loading } = this.props;
    const { dataList, total } = pipeline;
    let { taskId, popup, currentPage } = this.state;
    const taskList = task.dataList;

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
        width: 180,
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
              <Icon
                title='添加region'
                type="appstore"
                style={{ marginLeft: 20, color: 'orange' }}
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
                title='添加同步规则'
                type="profile"
                style={{ marginLeft: 20, color: 'green' }}
                onClick={() => {
                  this.toSync(record);
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
                value={taskId || ''}
                style={{ width: 150 }}
                onChange={this.searchOnSelectchange.bind(this, 'taskId')}
                placeholder="请选择任务"
              >
                {taskList.map((item, index) => {
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
