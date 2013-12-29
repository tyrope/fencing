package nl.tyrope.fencing.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class FenceXsectionModel extends ModelBase {
	// fields
	ModelRenderer postLeft;
	ModelRenderer postRight;
	ModelRenderer bottomTopLeft;
	ModelRenderer bottomBottomMiddle;
	ModelRenderer bottomTopRight;
	ModelRenderer top;
	ModelRenderer middle;
	ModelRenderer bottom;
	ModelRenderer topTopLeft;
	ModelRenderer topTopRight;
	ModelRenderer topBottomMiddle;
	ModelRenderer middleTopLeft;
	ModelRenderer middleTopRight;
	ModelRenderer middleBottomMiddle;

	public FenceXsectionModel() {
		textureWidth = 32;
		textureHeight = 32;

		postLeft = new ModelRenderer(this, 0, 2);
		postLeft.addBox(-1F, -8F, -1F, 2, 16, 2);
		postLeft.setRotationPoint(-7F, 16F, 0F);
		postLeft.setTextureSize(32, 32);
		postLeft.mirror = true;
		setRotation(postLeft, 0F, 0F, 0F);
		postRight = new ModelRenderer(this, 0, 2);
		postRight.addBox(-1F, -8F, -1F, 2, 16, 2);
		postRight.setRotationPoint(0F, 16F, 7F);
		postRight.setTextureSize(32, 32);
		postRight.mirror = true;
		setRotation(postRight, 0F, 0F, 0F);
		bottomTopLeft = new ModelRenderer(this, 8, 2);
		bottomTopLeft.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		bottomTopLeft.setRotationPoint(-5F, 17F, 2F);
		bottomTopLeft.setTextureSize(32, 32);
		bottomTopLeft.mirror = true;
		setRotation(bottomTopLeft, 0F, -0.7853982F, 0F);
		bottomBottomMiddle = new ModelRenderer(this, 8, 2);
		bottomBottomMiddle.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		bottomBottomMiddle.setRotationPoint(-3.5F, 19F, 3.5F);
		bottomBottomMiddle.setTextureSize(32, 32);
		bottomBottomMiddle.mirror = true;
		setRotation(bottomBottomMiddle, 0F, -0.7853982F, 0F);
		bottomTopRight = new ModelRenderer(this, 8, 2);
		bottomTopRight.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		bottomTopRight.setRotationPoint(-2F, 17F, 5F);
		bottomTopRight.setTextureSize(32, 32);
		bottomTopRight.mirror = true;
		setRotation(bottomTopRight, 0F, -0.7853982F, 0F);
		top = new ModelRenderer(this, 0, 0);
		top.addBox(-4.5F, -0.5F, -0.5F, 9, 1, 1);
		top.setRotationPoint(-3.5F, 12F, 3.5F);
		top.setTextureSize(32, 32);
		top.mirror = true;
		setRotation(top, 0F, -0.7853982F, 0F);
		middle = new ModelRenderer(this, 0, 0);
		middle.addBox(-4.5F, -0.5F, -0.5F, 9, 1, 1);
		middle.setRotationPoint(-3.5F, 15F, 3.5F);
		middle.setTextureSize(32, 32);
		middle.mirror = true;
		setRotation(middle, 0F, -0.7853982F, 0F);
		bottom = new ModelRenderer(this, 0, 0);
		bottom.addBox(-4.5F, -0.5F, -0.5F, 9, 1, 1);
		bottom.setRotationPoint(-3.5F, 18F, 3.5F);
		bottom.setTextureSize(32, 32);
		bottom.mirror = true;
		setRotation(bottom, 0F, -0.7853982F, 0F);
		topTopLeft = new ModelRenderer(this, 8, 2);
		topTopLeft.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		topTopLeft.setRotationPoint(-5F, 11F, 2F);
		topTopLeft.setTextureSize(32, 32);
		topTopLeft.mirror = true;
		setRotation(topTopLeft, 0F, -0.7853982F, 0F);
		topTopRight = new ModelRenderer(this, 8, 2);
		topTopRight.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		topTopRight.setRotationPoint(-2F, 11F, 5F);
		topTopRight.setTextureSize(32, 32);
		topTopRight.mirror = true;
		setRotation(topTopRight, 0F, -0.7853982F, 0F);
		topBottomMiddle = new ModelRenderer(this, 8, 2);
		topBottomMiddle.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		topBottomMiddle.setRotationPoint(-3.5F, 13F, 3.5F);
		topBottomMiddle.setTextureSize(32, 32);
		topBottomMiddle.mirror = true;
		setRotation(topBottomMiddle, 0F, -0.7853982F, 0F);
		middleTopLeft = new ModelRenderer(this, 8, 2);
		middleTopLeft.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		middleTopLeft.setRotationPoint(-5F, 14F, 2F);
		middleTopLeft.setTextureSize(32, 32);
		middleTopLeft.mirror = true;
		setRotation(middleTopLeft, 0F, -0.7853982F, 0F);
		middleTopRight = new ModelRenderer(this, 8, 2);
		middleTopRight.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		middleTopRight.setRotationPoint(-2F, 14F, 5F);
		middleTopRight.setTextureSize(32, 32);
		middleTopRight.mirror = true;
		setRotation(middleTopRight, 0F, -0.7853982F, 0F);
		middleBottomMiddle = new ModelRenderer(this, 8, 2);
		middleBottomMiddle.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 1);
		middleBottomMiddle.setRotationPoint(-3.5F, 16F, 3.5F);
		middleBottomMiddle.setTextureSize(32, 32);
		middleBottomMiddle.mirror = true;
		setRotation(middleBottomMiddle, 0F, -0.7853982F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		postLeft.render(f5);
		postRight.render(f5);
		bottomTopLeft.render(f5);
		bottomBottomMiddle.render(f5);
		bottomTopRight.render(f5);
		top.render(f5);
		middle.render(f5);
		bottom.render(f5);
		topTopLeft.render(f5);
		topTopRight.render(f5);
		topBottomMiddle.render(f5);
		middleTopLeft.render(f5);
		middleTopRight.render(f5);
		middleBottomMiddle.render(f5);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3,
			float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
