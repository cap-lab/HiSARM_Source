#ifndef __SEMO_SIMULATION_CLIENT_HEADER__
#define __SEMO_SIMULATION_CLIENT_HEADER__
#include "semo_simulation.h"
#include "RemoteAPIClient.h"

class RemoteAPIClientWrapper {
private:
    //static std::mutex instance_mtx;   // Mutex for thread-safety during object creation
    //static RemoteAPIClientWrapper* instance;

    RemoteAPIClient client;
    std::mutex client_mtx;  // Mutex for thread-safety during client.call()

    bool started = false;
    std::mutex started_mtx;

    RemoteAPIClientWrapper() : client(SEMO_SIMULATOR_IP, SEMO_SIMULATOR_PORT, -1, -1) {}

public:
    RemoteAPIClientWrapper(const RemoteAPIClientWrapper&) = delete;
    RemoteAPIClientWrapper& operator=(const RemoteAPIClientWrapper&) = delete;

    static RemoteAPIClientWrapper* getInstance() {
        //if (!instance) {
        //    std::lock_guard<std::mutex> lock(instance_mtx);
        //    if (!instance) {
        //        instance = new RemoteAPIClientWrapper();
        //    }
        //}
        //return instance;
        return new RemoteAPIClientWrapper();
    }

    // Thread-safe call() method that delegates the call to the client's call() method
    json call(std::string command, json args) {
         std::lock_guard<std::mutex> lock(client_mtx);
        return client.call(command, args);
    }

    void start() {
        std::lock_guard<std::mutex> lock(started_mtx);
        if (started == false){
            json _args(json_array_arg);
            started = true;
            client.step(false);
            this->call("sim.startSimulation", _args);
        }
    }
};

#endif