'use strict';

myApp.factory('UserService', ['$resource',
    function ($resource) {
        return $resource('http://localhost:8080/api/user/me');
    }]);

myApp.factory('UserPasswordService', ['$http',
    function ($http) {
        return {
            changePassword: function (oldPassword, newPassword, cb, errorCb) {
                return $http({
                    method: 'POST',
                    url: "http://localhost:8080/api/user/password",
                    data: $.param({oldPassword: oldPassword, newPassword: newPassword}),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function (result) {
                    if (cb) {
                        cb(result);
                    }
                }).error(function (result) {
                    if (errorCb) {
                        errorCb(result);
                    }
                });
            }
        }

    }]);


myApp.factory('UserRegisterService', ['$resource',
    function ($resource) {
        return $resource('http://localhost:8080/api/user/register', {}, {
            register: {method: 'POST'}
        });
    }]);

myApp.factory('MealService', ['$resource',
    function ($resource) {
        return $resource('http://localhost:8080/api/meal/:mealId/:version', {}, {
            save: {method: 'POST', params: {mealId:'@id'}},
            query: {method: 'GET', params: {}, isArray: true},
            all: {method: 'GET', params: {mealId: 'all'}, isArray: true},
            find: {method: 'GET', params: {mealId: 'find'}, isArray: true}
        });
    }]);


myApp.factory('AuthService', ['$http', 'UserService',
        function ($http, UserService) {
            var currentUser = null;
            var authorized = false;
            var initialState = true;
            var inProgress = false;

            var login = function (username, password, cb, errorCb) {
                if (authorized) {
                    return;
                }
                $http({
                    method: 'POST',
                    url: "http://localhost:8080/auth/login",
                    data: $.param({username: username, password: password}),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).then(function (response) {
                    //after a successful login you get a redirect to /
                    //ends with ?error
                    if (response.data.indexOf('Bad credentials') != -1) {
                        if (errorCb) {
                            errorCb();
                        }
                    } else {
                        UserService.get(function (result) {
                            if (result && result.id) {
                                setCurrentUser(result);
                            }
                        });

                        if (cb) {
                            cb();
                        }
                    }

                }, function (response) {
                    if (errorCb) {
                        errorCb();
                    }
                });
            };

            function setCurrentUser(user) {
                currentUser = user;
                authorized = true;
                initialState = false;
            }

            return {
                initialState: function () {
                    return initialState;
                },
                login: login,
                logout: function () {
                    $http.get("http://localhost:8080/auth/logout").success(function () {
                        currentUser = null;
                        authorized = false;
                    });
                },
                tryLogin: function (cb) {

                    if (inProgress) {
                        return;
                    }

                    if (authorized) {
                        if (cb) {
                            cb(currentUser);
                        }
                        return;
                    }
                    inProgress = true;
                    UserService.get({}, function (result) {
                        if (result && result.id) {
                            setCurrentUser(result);
                        }
                        if (cb) {
                            cb(currentUser);
                        }
                        inProgress = false;
                        initialState = false;
                    }, function () {
                        if (cb) {
                            cb(currentUser);
                        }
                        inProgress = false;
                        initialState = false;
                    });
                },
                currentUser: function () {
                    return currentUser;
                },
                setCurrentUser: function (user) {
                    currentUser = user;
                },
                authorized: function () {
                    return authorized;
                }
            };
        }]
);