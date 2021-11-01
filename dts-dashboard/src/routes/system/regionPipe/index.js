import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Select } from "antd";
import { connect } from "dva";
import { parse } from "qs";
import AddModal from './AddModal';

const { Option } = Select;
@connect(({ pipeline, regionPipe, loading }) => ({
  pipeline,
  regionPipe,
  loading: loading.effects["regionPipe/fetch"],
}))
export default class Region extends PureComponent {
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
      type: "regionPipe/saveList",
      payload: {
        dataList: [],
        total: 0
      }
    });
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

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  searchOnSelectchange = (key, value) => {
    this.setState({ [key]: value });
  };

  listItems = page => {
    const { dispatch } = this.props;
    const { pipelineId } = this.state;
    if (!pipelineId) return;
    dispatch({
      type: "regionPipe/fetch",
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



  searchOnchange = (key, e) => {
    const value = e.target.value;
    this.setState({ [key]: value });
  };

  searchClick = () => {
    this.listItems(1);
    this.setState({ currentPage: 1 });
  };

  deleteClick = (item) => {
    const { dispatch } = this.props;
    const { currentPage, pipelineId } = this.state;
    dispatch({
      type: "regionPipe/delete",
      payload: {
        id: item.id
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


  editClick = (item) => {
    const { dispatch } = this.props;
    const { currentPage } = this.state;
    this.setState({
      popup: (
        <AddModal
          {...item}
          disabled={false}
          handleOk={values => {
            dispatch({
              type: "regionPipe/update",
              payload: { ...values },
              fetchValue: {
                pipelineId: values.pipelineId,
                pageNum: currentPage,
                pageSize: this.pageSize
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
          handleCancel={this.closeModal}
        />
      )
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
              type: "regionPipe/add",
              payload: { ...values },
              callback: () => {
                this.setState({ pipelineId: values.pipelineId, selectedRowKeys: [] }, this.searchClick);
                this.closeModal();
              }
            });
          }}
          handleCancel={this.closeModal}
        />
      )
    });
  };


  filterOption = (input, option) => option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0


  render() {

    const { regionPipe, loading, pipeline } = this.props;
    const { dataList: pipelineList } = pipeline;
    const { dataList, total } = regionPipe;
    const { currentPage, popup, pipelineId } = this.state;

    const tableColumns = [
      {
        align: "center",
        title: "Region",
        dataIndex: "region",
        key: "region",
      },
      {
        align: "center",
        title: "Mode",
        dataIndex: "mode",
        key: "mode",
      },
      {
        align: "center",
        title: "通道Id",
        dataIndex: "pipelineId",
        key: "pipelineId",
      },
      {
        align: "center",
        title: "通道名",
        dataIndex: "pipelineName",
        key: "pipelineName",
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
                <Breadcrumb.Item>Region映射列表</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="table-header" style={{ justifyContent: "normal" }}>
              <Select
                showSearch={true}
                filterOption={this.filterOption}
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
    )
      ;
  }
}
