test_home=cs455/overlay/node/*

for i in `cat machine_list`
do
	echo 'logging into '${i}
	gnome-terminal -x bash -c "ssh -t ${i} 'cd $test_home; java cs455.overlay.node.MessagingNode austin 7688;bash;'" &
done
