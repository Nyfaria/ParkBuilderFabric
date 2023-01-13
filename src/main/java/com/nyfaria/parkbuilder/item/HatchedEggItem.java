package com.nyfaria.parkbuilder.item;

import com.nyfaria.parkbuilder.entity.ai.nums.Gender;
import com.nyfaria.parkbuilder.entity.ai.nums.GrowthStages;
import com.nyfaria.parkbuilder.entity.dinos.DinoBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class HatchedEggItem<T extends Entity> extends Item {

    private final EntityType<T> defaultType;

    public HatchedEggItem(EntityType<T> pDefaultType, Item.Properties pProperties) {
        super(pProperties);
        this.defaultType = pDefaultType;

    }

    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = pContext.getItemInHand();
            BlockPos blockpos = pContext.getClickedPos();
            Direction direction = pContext.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(Blocks.SPAWNER)) {
                BlockEntity blockentity = level.getBlockEntity(blockpos);
                if (blockentity instanceof SpawnerBlockEntity) {
                    BaseSpawner basespawner = ((SpawnerBlockEntity) blockentity).getSpawner();
                    EntityType<?> entitytype1 = this.getType(itemstack.getTag());
                    basespawner.setEntityId(entitytype1);
                    blockentity.setChanged();
                    level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    level.gameEvent(pContext.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            EntityType<?> entitytype = this.getType(itemstack.getTag());
            DinoBase entity = (DinoBase) entitytype.create(level);//, itemstack, pContext.getPlayer(), blockpos1, MobSpawnType.BREEDING, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (entity != null) {
                entity.setPos(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D);

                if (itemstack.getOrCreateTag().contains("male")) {
                     entity.setGender(itemstack.getOrCreateTag().getBoolean("male") ? Gender.MALE : Gender.FEMALE);
                } else {
                    entity.setGender(Gender.byId(level.random.nextInt(Gender.values().length)));
                }

                entity.setGrowth(GrowthStages.HATCHLING);

            }
            level.addFreshEntity(entity);
            entity.refreshDimensions();
            itemstack.shrink(1);
            level.gameEvent(pContext.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
        }

        return InteractionResult.CONSUME;
    }



    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        HitResult hitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
        if (hitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else if (!(pLevel instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemstack);
        } else {
            BlockHitResult blockhitresult = (BlockHitResult) hitresult;
            BlockPos blockpos = blockhitresult.getBlockPos();
            if (!(pLevel.getBlockState(blockpos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemstack);
            } else if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos, blockhitresult.getDirection(), itemstack)) {
                EntityType<?> entitytype = this.getType(itemstack.getTag());
                DinoBase entity = (DinoBase) entitytype.create(pLevel);//, itemstack, pContext.getPlayer(), blockpos1, MobSpawnType.BREEDING, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
                if (entity != null) {
                    entity.setPos(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);

                    if (itemstack.getOrCreateTag().contains("male")) {
                        entity.setGender(itemstack.getOrCreateTag().getBoolean("male") ? Gender.MALE : Gender.FEMALE);
                    } else {
                        entity.setGender(Gender.byId(pLevel.random.nextInt(Gender.values().length)));
                    }
                    entity.setGrowth(GrowthStages.HATCHLING);

                }
                if (entity == null) {
                    return InteractionResultHolder.pass(itemstack);
                } else {
                    pLevel.addFreshEntity(entity);
                    entity.refreshDimensions();
                    if (!pPlayer.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }

                    pPlayer.awardStat(Stats.ITEM_USED.get(this));
                    pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, entity.position());
                    return InteractionResultHolder.consume(itemstack);
                }
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    public boolean spawnsEntity(@Nullable CompoundTag pNbt, EntityType<?> pType) {
        return Objects.equals(this.getType(pNbt), pType);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @org.jetbrains.annotations.Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pStack.getOrCreateTag().contains("male")) {
            pTooltipComponents.add(pStack.getOrCreateTag().getBoolean("male") ? Component.translatable("gender.male") : Component.translatable("gender.female"));
        }
    }

    public EntityType<?> getType(@Nullable CompoundTag pNbt) {
        if (pNbt != null && pNbt.contains("EntityTag", 10)) {
            CompoundTag compoundtag = pNbt.getCompound("EntityTag");
            if (compoundtag.contains("id", 8)) {
                return EntityType.byString(compoundtag.getString("id")).orElse(this.defaultType);
            }
        }

        return this.defaultType;
    }

    public Optional<Mob> spawnOffspringFromSpawnEgg(Player pPlayer, Mob pMob, EntityType<? extends Mob> pEntityType, ServerLevel pServerLevel, Vec3 pPos, ItemStack pStack) {
        if (!this.spawnsEntity(pStack.getTag(), pEntityType)) {
            return Optional.empty();
        } else {
            Mob mob;
            if (pMob instanceof AgeableMob) {
                mob = ((AgeableMob) pMob).getBreedOffspring(pServerLevel, (AgeableMob) pMob);
            } else {
                mob = pEntityType.create(pServerLevel);
            }

            if (mob == null) {
                return Optional.empty();
            } else {
                mob.setBaby(true);
                if (!mob.isBaby()) {
                    return Optional.empty();
                } else {
                    mob.moveTo(pPos.x(), pPos.y(), pPos.z(), 0.0F, 0.0F);
                    pServerLevel.addFreshEntityWithPassengers(mob);
                    if (pStack.hasCustomHoverName()) {
                        mob.setCustomName(pStack.getHoverName());
                    }

                    if (!pPlayer.getAbilities().instabuild) {
                        pStack.shrink(1);
                    }

                    return Optional.of(mob);
                }
            }
        }
    }

    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (this.allowedIn(pCategory)) {
            ItemStack basic = new ItemStack(this);
            ItemStack male = new ItemStack(this);
            male.getOrCreateTag().putBoolean("male", true);
            ItemStack female = new ItemStack(this);
            female.getOrCreateTag().putBoolean("male", false);
            pItems.add(basic);
            pItems.add(male);
            pItems.add(female);
        }
    }
}
