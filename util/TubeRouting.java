package YATS.util;

import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import YATS.api.ITubeConnectable;
import YATS.api.ICapsule;
import YATS.util.LazUtils.XYZCoords;
import net.minecraft.inventory.IInventory;
import YATS.capsule.ItemCapsule;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TubeRouting {
	public World world;
	public PriorityQueue<TubeRoute> processqueue = new PriorityQueue<TubeRoute>();
	
	public TubeRouting(World world) {
		this.world = world;
	}
	
	public void ScanBlock(XYZCoords block, ForgeDirection initialdir, ForgeDirection heading, int distance, ICapsule capsule) {
	    if(!(world.blockHasTileEntity(block.x,block.y,block.z))) return;
	    else {
	      TileEntity tile = world.getBlockTileEntity(block.x,block.y, block.z);
	      if (tile instanceof ITubeConnectable)
	        processqueue.add(new TubeRoute(block, heading, initialdir, distance + ((ITubeConnectable)tile).GetAdditionalPriority()));
	      else if (tile instanceof IInventory && capsule instanceof ItemCapsule && LazUtils.InventoryCore.CanAddToInventory(block, (ItemStack)(capsule.GetContents())))
	      {
	        TubeRoute route = new TubeRoute(block,heading,initialdir,distance);
	        route.completed = true;
	        processqueue.add(route);
	      }
	    }
	}
	
	public ForgeDirection FindRoute(XYZCoords block, ForgeDirection initial, List<ForgeDirection> sides, ICapsule capsule) {
		for(ForgeDirection side: sides) {
			XYZCoords newBlock = block.Next(side);
			ScanBlock(newBlock, initial, initial, (side == initial) ? 0 : 1, capsule);
		}
		
		while(processqueue.size() > 0) {
			TubeRoute route = processqueue.poll();
			if(route.completed) return route.initialdir;
			
			for(ForgeDirection side: ForgeDirection.VALID_DIRECTIONS) {
				XYZCoords newCoords = route.destination.Next(side);
				ScanBlock(newCoords, initial, side, route.distance+1, capsule);
			}
		}
		return ForgeDirection.UNKNOWN;
	}
}