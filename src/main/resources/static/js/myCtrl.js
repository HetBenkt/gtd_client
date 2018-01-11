app.controller('myCtrl', function ($scope, $http) {

    $scope.initPage = function () {
        $http.get("http://localhost:9080/boards/search/findByMemberId?id=1")
            .then(function mySuccess(response) {
                console.log('findByMemberId', response);
                $scope.boards = response.data._embedded.boards;
                switchBoard(response.data._embedded.boards[0].id);
            }, function myError(response) {
                console.log(response.statusText);
            });
    };

    $scope.switchBoard = function (id) {
        switchBoard(id);
    };

    var switchBoard = function (id) {
        console.log('Selected id', id);
        $http.get("http://localhost:9080/boards/" + id + "/columns")
            .then(function mySuccess(response) {
                console.log('columns', response.data);
                $scope.boardData = [];
                for (i = 0; i < response.data._embedded.columns.length; i++) {
                    getCards(response.data._embedded.columns[i].id, response.data._embedded.columns[i].name);
                }
            }, function myError(response) {
                console.log(response.statusText);
            });
    };

    var getCards = function (id, name) {
        console.log('Column id', id);
        $http.get("http://localhost:9080/cards/search/findByColumnId?id=" + id)
            .then(function mySuccess(response) {
                console.log('findByColumnId', response);
                $scope.boardData.push({card: response, column: {id: id, name: name}});
            }, function myError(response) {
                console.log(response.statusText);
            });
    };

    $scope.addTaskModal = function (columnName) {
        $scope.taskModalHeader = columnName;
        console.log('Column', columnName);
    };

    $scope.addLabel = function (labelName) {
        var elem = angular.element(labels);
        elem.append('<li class="list-group-item" id="' + labelName + '"><button onclick="removeLabel(' + labelName + ')" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-remove-sign"></span></button> ' + labelName + '</li>');
        console.log('AddLabel', elem);
    };
});

function removeLabel(labelName) {
    var elem = angular.element(labelName);
    elem.remove();
    console.log('RemoveLabel', elem);
};