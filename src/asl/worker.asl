// Agent worker in project warehouse

/* Beliefs */
battery_capacity(1000).
battery(1000).
status(idle).

/* Rules */

is_battery_too_low :- battery(X) & X < 100.

is_working :- status(STATUS) & STATUS = busy.

has_enough_power(TASK_DISTANCE, DISTANCE_TO_CHARGER) :- battery(X) & (TASK_DISTANCE + DISTANCE_TO_CHARGER) < X.

/* Plans */

//!charge_battery.

+at(Place): true
		<-  -+atPlace(Place).

//+status(STATUS): STATUS = busy
//		<-  .print("Start working")
//			!engine_working.

+bid(TASK_ID, DISTANCE, TIME, EXIT_ID, RES_ID) : true
		<- 	.print("Receive BID= ", TASK_ID, " ", DISTANCE, " ", TIME, " ", EXIT_ID, " ", RES_ID);
			.my_name(NAME);
			if(has_enough_power(DISTANCE, 20)) {
				.send(m, tell, place_bid(TASK_ID, DISTANCE, EXIT_ID, RES_ID, NAME));
			} else {
				.send(m, tell, place_bid(TASK_ID, busy, busy, busy, NAME));
			}.

+taskAuction(TASK_ID, EXIT_ID, RES_TYPE_ID) : status(idle)
		<-  .my_name(NAME);
			internalactions.sendMessageBid(NAME, TASK_ID, EXIT_ID, RES_TYPE_ID);
			.abolish(taskAuction(_,_,_)).

//FOR TESTING ONLY
//+taskAuction(TaskId, ExitId, ResourceType) : status(idle)
//			<- .my_name(Name);
//			   .random(R); X = 10*R;
//			   .send(m, tell, place_bid(TaskId, X, ExitId, ResourceType, Name));
//			   .abolish(taskAuction(_,_,_)).


+taskAuction(TASK_ID, EXIT_ID, RES_TYPE_ID) : status(busy) | status(charging)
		<-  .my_name(NAME);
			.send(m, tell, place_bid(TASK_ID, busy, busy, busy, NAME));
			.abolish(taskAuction(_,_,_)).


+task(TASK_ID, EXIT_ID, RES_ID, RES_TYPE_ID) : status(idle)
		<-	.print("I AM WIN Auction ", TASK_ID);
			!completeTask(TASK_ID, RES_ID, EXIT_ID, RES_TYPE_ID).

/* Actions */

+!goto(PLACE) : true 
		<-	.my_name(NAME);
			internalactions.sendMessageGoto(NAME, PLACE);
			.wait(atPlace(PLACE)).
								
+!completeTask(TASK_ID, RES_ID, EXIT_ID, RES_TYPE_ID) : status(idle) & bid(TASK_ID, DISTANCE, TIME, EXIT_ID, RES_ID)
		<-	-+status(busy);
 			!goto(RES_ID);
 			!pick_up(RES_TYPE_ID, 1);
			!goto(EXIT_ID);
			!put_down(RES_TYPE_ID, 1);
			-+status(idle);
			?battery(X);
			-+battery(X-DISTANCE);
			-bid(TASK_ID, DISTANCE, TIME, EXIT_ID, RES_ID)[source(_)];
			internalactions.sendResourceDelivered(EXIT_ID, RES_TYPE_ID);
			if(is_battery_too_low) {
				!charge_battery;
			}
			.print("TASK DONE id= ", TASK_ID).

+!pick_up(RES_TYPE_ID, QUANTITY) : true
		<-  .wait(200);
			.print("Pick up ", RES_TYPE_ID, " quantity ", QUANTITY).
		
+!put_down(RES_TYPE_ID, QUANTITY) : true
		<-  .wait(200);
			.print("Pick up ", RES_TYPE_ID, " quantity ", QUANTITY).

//+!engine_working : true 
//		<-  .print("Working...");
//			while(is_working) {
//				?battery(X);
//				-+battery(X-1);
//				.wait(500);	
//			};
//			.print("Stop working").
				
+!charge_battery : true
		<- .print("Charge battery");
		-+status(busy);
		//!goto(X);
		//send message charge
		-+status(charging);
		?battery(X);
		?battery_capacity(Y);
		.print("Battery ", X);
		.print("Battery capacity ", Y);
		for ( .range(I,X,Y) ) {
        	.print(I);
        	.wait(100);
     	}
		-+battery(Y);
		-+staus(idle);
		.print("End charging").
