TASK_CODE_BEGIN

TASK_INIT
{
	LOG_INFO("INIT");
    port_init();
    service_init();
    event_list_init();
}

TASK_GO
{
	transition_id = check_group_allocation();
	if (transition_id != -1)
	{
		run_transition(transition_id);
	}

	execute_services();
	action_state_polling();
	
	if (event_occured == TRUE) 
	{
	    manage_event();
	    service_wrapup(THIS_TASK_ID);
	    event_occured = FALSE;
	}
}

TASK_WRAPUP
{
	LOG_INFO("WRAP UP");
    service_reset(THIS_TASK_ID);
}

TASK_CODE_END
