/* Initial beliefs and rules */


/* Rules */

all_bids_received(TaskId) :- .all_names(X) & .length(X, Y) & .count(place_bid(TaskId, _, _, _, _), (Y-1)).

is_any_worker :- .all_names(X) & .length(X, Y) & Y > 1.

!start_auction.

/* Actions */

+task(TASK_ID, EXIT_ID, RES_TYPE_ID): true 
		<-  +my_task(TASK_ID, EXIT_ID, RES_TYPE_ID, "ready");
			-task(TASK_ID, EXIT_ID, RES_TYPE_ID)[source(percept)].

+!start_auction : .findall(my_task(TASK_ID, EXIT_ID, RES_TYPE_ID, STATUS), (my_task(TASK_ID, EXIT_ID, RES_TYPE_ID, STATUS) & STATUS = "ready"), L) & not .empty(L) & is_any_worker
		<-  .sort(L, L_SORTED);
			.print("SORTED LIST ", L_SORTED);
			.nth(0,L_SORTED,TASK);
			.print("TASK TO AUCTION ", TASK);
			my_task(TaskId, ExitId, ResourceType, Status) =  TASK;
			.broadcast(tell, taskAuction(TaskId, ExitId, ResourceType));
			.all_names(NAMES); 
			.length(NAMES, NAMES_LENGTH);
			.wait(.count(place_bid(TaskId, _, _, _, _), (NAMES_LENGTH-1)));
			.findall(place_bid(TaskId_, Distance_, ExitId_, ResourceId_, SourceAgent_), place_bid(TaskId_, Distance_, ExitId_, ResourceId_, SourceAgent_) & TaskId_ == TaskId, List);
			.sort(List, ListSorted);
			.nth(0, ListSorted, WINNER);
			place_bid(TId1, Dist1, DestId1, ResId1, AgName1) = WINNER;
			if(not Dist1 == busy) {
				.send(AgName1, tell, task(TId1, DestId1, ResId1, ResourceType));
				-my_task(TaskId, ExitId, ResourceType, "ready");
				+my_task(TaskId, ExitId, ResourceType, "done");
			} else {
				.print("No winner");
			}
			.abolish(place_bid(TaskId,_,_,_,_));
			!start_auction.

+!start_auction : true
		<-  .abolish(my_task(_, _, _, "done"));
			.wait(1000);
			!start_auction.
