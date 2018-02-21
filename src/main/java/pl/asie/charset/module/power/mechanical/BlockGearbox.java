package pl.asie.charset.module.power.mechanical;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import pl.asie.charset.lib.block.BlockBase;

import javax.annotation.Nullable;

public class BlockGearbox extends BlockBase implements ITileEntityProvider {
	public BlockGearbox() {
		super(Material.WOOD);
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileGearbox();
	}
}