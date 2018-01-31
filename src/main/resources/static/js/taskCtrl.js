app.controller('taskCtrl', function ($scope, $http) {
    $scope.labels = [];

    $scope.addLabel = function (labelName) {
        console.log('AddLabel', labelName);
        $scope.labels.indexOf(labelName) === -1 ? $scope.labels.push(labelName) : console.log("Label already exists");
    };

    $scope.removeLabel = function (labelName) {
        console.log('RemoveLabel', labelName);
        $scope.labels.splice($scope.labels.indexOf(labelName), 1);
    };

    $scope.saveTask = function (columnId, taskName, taskDescription) {
        console.log('saveTask', columnId, taskName, taskDescription, $scope.labels);
        $http({
            method: "POST",
            url: "http://localhost:9080/cards",
            data: {
                "name": taskName,
                "description": taskDescription,
                "labels": $scope.labels
            }
        }).then(function onOk(response) {
            console.log('Task is saved', response);
            //$scope.boardData[columnId-1].card.data._embedded.cards.push(response.data);
            //console.log('Data', $scope.boardData[columnId-1].card.data._embedded.cards);
            $http({
                method: "PUT",
                url: response.data._links.column.href,
                data: "http://localhost:9080/columns/" + columnId,
                headers: {
                    'Content-Type': 'text/uri-list'
                }
            }).then(function onOk(response) {
                console.log('Task is related', response);
            }, function onError(response) {
                console.log(response.statusText);
            });
            //switchBoard($scope.currentBoardId);
        }, function onError(response) {
            console.log(response.statusText);
        });
        resetTaskModalData();
    }

    var resetTaskModalData = function () {
        console.log('resetTaskModal',  $scope.taskName);
        $scope.taskName = null;
        $scope.addTaskForm.$setPristine();
        // $scope.taskName = '';
        // $scope.taskDesciption = '';
        // $scope.label = '';
    }
});