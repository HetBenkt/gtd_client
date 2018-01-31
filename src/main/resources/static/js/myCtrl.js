app.controller('myCtrl', function ($scope, $http) {
    $scope.currentBoardId = 0;

    $scope.initPage = function () {
        $http.get("http://localhost:9080/boards/search/findByMemberId?id=1")
            .then(function onOk(response) {
                console.log('findByMemberId', response);
                $scope.boards = response.data._embedded.boards;
                $scope.currentBoardId = response.data._embedded.boards[0].id;
                switchBoard(response.data._embedded.boards[0].id);
            }, function onError(response) {
                console.log(response.statusText);
            });
    };

    $scope.switchBoard = function (id) {
        $scope.currentBoardId = id;
        switchBoard(id);
    };

    var switchBoard = function (id) {
        console.log('Selected id', id);
        $http.get("http://localhost:9080/boards/" + id + "/columns")
            .then(function onOk(response) {
                console.log('columns', response.data);
                $scope.boardData = [];
                for (var i = 0; i < response.data._embedded.columns.length; i++) {
                    getCards(response.data._embedded.columns[i].id, response.data._embedded.columns[i].name);
                }
            }, function onError(response) {
                console.log(response.statusText);
            });
    };

    var getCards = function (id, name) {
        console.log('Column id', id);
        $http.get("http://localhost:9080/cards/search/findByColumnId?id=" + id)
            .then(function onOk(response) {
                console.log('findByColumnId', response);
                $scope.boardData.push({card: response, column: {id: id, name: name}});
            }, function onError(response) {
                console.log(response.statusText);
            });
    };

    $scope.addTaskModal = function (columnId, columnName) {
        $scope.taskModalHeader = columnName;
        $scope.taskModalHeaderId = columnId;
        console.log('Column', columnId, columnName);
    };
});