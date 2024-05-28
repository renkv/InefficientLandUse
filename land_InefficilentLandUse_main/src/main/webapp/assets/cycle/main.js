'use strict';

(function($){

    $(function() {

        var datascource = {
            'name': '全部低效土地',
            'title': '面积：2222333.225<br/>数量：30000',
            'children': [
                { 'name': '收储再开发', 'title': '面积：333.225<br/>数量：25', 'className': 'middle-level',
                    'children': [
                        { 'name': '拟收储', 'title': '面积：3.225<br/>数量：1', 'className': 'product-dept' },
                        { 'name': '已收储', 'title': '面积：0<br/>数量：0', 'className': 'product-dept'},
                        { 'name': '待开发', 'title': '面积：0<br/>数量：0', 'className': 'product-dept'},
                        { 'name': '已开发', 'title': '面积：0<br/>数量：0', 'className': 'product-dept'},
                    ]
                },
                { 'name': '自主开发', 'title': '面积：0<br/>数量：0', 'className': 'middle-level',
                    /*'children': [
                        { 'name': 'Pang Pang', 'title': 'senior engineer', 'className': 'rd-dept' },
                        { 'name': 'Hei Hei', 'title': 'senior engineer', 'className': 'rd-dept',
                            'children': [
                                { 'name': 'Xiang Xiang', 'title': 'UE engineer', 'className': 'frontend1' },
                                { 'name': 'Dan Dan', 'title': 'engineer', 'className': 'frontend1' },
                                { 'name': 'Zai Zai', 'title': 'engineer', 'className': 'frontend1' }
                            ]
                        }
                    ]*/
                },
                { 'name': '复垦耕地', 'title': '面积：0<br/>数量：0', 'className': 'middle-level',
                    /*'children': [
                        { 'name': 'Pang Pang', 'title': 'senior engineer', 'className': 'rd-dept' },
                        { 'name': 'Hei Hei', 'title': 'senior engineer', 'className': 'rd-dept',
                            'children': [
                                { 'name': 'Xiang Xiang', 'title': 'UE engineer', 'className': 'frontend1' },
                                { 'name': 'Dan Dan', 'title': 'engineer', 'className': 'frontend1' },
                                { 'name': 'Zai Zai', 'title': 'engineer', 'className': 'frontend1' }
                            ]
                        }
                    ]*/
                },
                { 'name': '技术提升', 'title': '面积：0<br/>数量：0', 'className': 'middle-level',
                    /*'children': [
                        { 'name': 'Pang Pang', 'title': 'senior engineer', 'className': 'rd-dept' },
                        { 'name': 'Hei Hei', 'title': 'senior engineer', 'className': 'rd-dept',
                            'children': [
                                { 'name': 'Xiang Xiang', 'title': 'UE engineer', 'className': 'frontend1' },
                                { 'name': 'Dan Dan', 'title': 'engineer', 'className': 'frontend1' },
                                { 'name': 'Zai Zai', 'title': 'engineer', 'className': 'frontend1' }
                            ]
                        }
                    ]*/
                },{ 'name': '司法处置或转让', 'title': '面积：0<br/>数量：0', 'className': 'middle-level',
                    /*'children': [
                        { 'name': 'Pang Pang', 'title': 'senior engineer', 'className': 'rd-dept' },
                        { 'name': 'Hei Hei', 'title': 'senior engineer', 'className': 'rd-dept',
                            'children': [
                                { 'name': 'Xiang Xiang', 'title': 'UE engineer', 'className': 'frontend1' },
                                { 'name': 'Dan Dan', 'title': 'engineer', 'className': 'frontend1' },
                                { 'name': 'Zai Zai', 'title': 'engineer', 'className': 'frontend1' }
                            ]
                        }
                    ]*/
                }
            ]
        };

        $('#chart-container').orgchart({
            'data' : datascource,
            'nodeContent': 'title'
        });

    });

})(jQuery);