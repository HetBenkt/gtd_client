app.controller('myCtrl', function($scope, $http) {

    $scope.initPage = function() {
        $http.get("http://localhost:9080/boards/search/findByMemberId?id=1")
            .then(function mySuccess(response) {
                console.log(response.data);
                $scope.boards = response.data._embedded.boards;
                switchBoard(response.data._embedded.boards[0].id);
            }, function myError(response) {
                console.log(response.statusText);
            });
    };

    $scope.switchBoard = function (id) {
        switchBoard(id);
    };

    var switchBoard = function(id) {
        console.log('Selected id', id);
        $http.get("http://localhost:9080/boards/"+id+"/columns")
            .then(function mySuccess(response) {
                console.log(response.data);
                $scope.columns = response.data._embedded.columns;
                for (i = 0; i < response.data._embedded.columns.length; i++) {
                    getCards(response.data._embedded.columns[i].id);
                }
            }, function myError(response) {
                console.log(response.statusText);
            });
    };

    var getCards = function (id) {
        console.log('Column id', id);
        $http.get("http://localhost:9080/cards/search/findByColumnId?id="+id)
            .then(function mySuccess(response) {
                console.log(response.data);
            }, function myError(response) {
                console.log(response.statusText);
            });
    };
});