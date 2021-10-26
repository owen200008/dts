import React, { PureComponent } from "react";
import { Table, Input, Button, Popconfirm, Icon, Col, Row, Breadcrumb, Switch } from "antd";
import { connect } from "dva";
import AddModal from './AddModal'


@connect(({ region, loading }) => ({
  region,
  loading: loading.effects["region/fetch"],
}))
export default class Region extends PureComponent {
  constructor(props) {
    super(props);

    this.state = {
      currentPage: 1,
      selectedRowKeys: [],
      region: '',
      popup: ""
    };
    this.pageSize = 10;
  }

  componentWillMount() {
    const { currentPage } = this.state;
    this.listItems(currentPage);
  }

  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  listItems = page => {
    const { dispatch } = this.props;
    const { region } = this.state;
    dispatch({
      type: "region/fetch",
      payload: {
        region: region || undefined,
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
    const { region, currentPage } = this.state;
    dispatch({
      type: "region/delete",
      payload: {
        regionId: item.id
      },
      fetchValue: {
        region: region || undefined,
        pageNum: currentPage,
        pageSize: this.pageSize
      },
      callback: () => {
        this.setState({ selectedRowKeys: [] });
      }
    });
  };


  addClick = () => {
    const { currentPage, region } = this.state;
    this.setState({
      popup: (
        <AddModal
          disabled={false}
          handleOk={values => {
            const { dispatch } = this.props;
            dispatch({
              type: "region/add",
              payload: { ...values },
              fetchValue: {
                region: region || undefined,
                pageNum: currentPage,
                pageSize: this.pageSize
              },
              callback: this.closeModal
            });
          }}
          handleCancel={this.closeModal}
        />
      )
    });
  };





  render() {

    const { region, loading } = this.props;
    const { dataList, total } = region;
    const { currentPage, region: regionName, popup } = this.state;

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
              <Popconfirm
                title="你确认删除吗"
                placement='bottom'
                onConfirm={() => {
                  this.deleteClick(record)
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
    ];



    return (
      <div className="plug-content-wrap">

        <Row gutter={24}>
          <Col span={24}>
            <div className="table-header" style={{ paddingTop: 5 }}>
              <Breadcrumb>
                <Breadcrumb.Item><a href="#/home">Home</a></Breadcrumb.Item>
                <Breadcrumb.Item>Region列表</Breadcrumb.Item>
              </Breadcrumb>
            </div>
            <div className="table-header" style={{ justifyContent: "normal" }}>
              <Input
                value={regionName}
                onChange={this.searchOnchange.bind(this, 'region')}
                placeholder="请输入名称"
                style={{ width: 240 }}
              />

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
