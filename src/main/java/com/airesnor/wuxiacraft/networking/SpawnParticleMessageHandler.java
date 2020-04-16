package com.airesnor.wuxiacraft.networking;

import com.airesnor.wuxiacraft.utils.SkillUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpawnParticleMessageHandler implements IMessageHandler<SpawnParticleMessage, IMessage> {
	@Override
	public IMessage onMessage(SpawnParticleMessage message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			return handleClientSide(message);
		}
		else if (ctx.side == Side.SERVER) {
			final EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() -> {
				SkillUtils.sendMessageWithinRange(player.getServerWorld(), message.getPos(), 64d, message);
			});
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public IMessage handleClientSide(SpawnParticleMessage message) {
		Minecraft.getMinecraft().addScheduledTask(() -> {
			Minecraft.getMinecraft().world.spawnParticle(message.getParticleType(),
					message.isIgnoreRange(),
					message.getPosX(), message.getPosY(), message.getPosZ(),
					message.getMotionX(), message.getMotionY(), message.getMotionZ(),
					message.getArguments());
		});
		return null;
	}
}
