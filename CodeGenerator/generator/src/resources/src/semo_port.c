#include "semo_logger.h"
#include "semo_port.h"

PORT *findPortListByTaskName(const PORT_INFO **service_task_port_list, int portNum, char *taskName, DIRECTION direction)
{
    PORT *result = NULL;
    LOG_DEBUG("Find %d direction ports of task %s", direction, taskName);
    for (int i = 0; i < portNum; i++)
    {
        if (!strcmp(service_task_port_list[i]->taskName, taskName))
        {
            if (direction == DIRECTION_IN)
            {
                result = service_task_port_list[i]->inputPortList;
            }
            else
            {
                result = service_task_port_list[i]->outputPortList;
            }
            break;
        }
    }
    return result;
}
