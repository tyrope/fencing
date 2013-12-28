package nl.tyrope.fencing.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BasicFenceModel extends ModelBase {
	// fields
	ModelRenderer postLeft;
	ModelRenderer postRight;
	ModelRenderer wireTop;
	ModelRenderer wireMiddle;
	ModelRenderer wireBottom;

	public BasicFenceModel() {
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
		postRight.setRotationPoint(7F, 16F, 0F);
		postRight.setTextureSize(32, 32);
		postRight.mirror = true;
		setRotation(postRight, 0F, 0F, 0F);
		wireTop = new ModelRenderer(this, 0, 0);
		wireTop.addBox(-6F, -0.5F, -0.5F, 12, 1, 1);
		wireTop.setRotationPoint(0F, 12F, 0F);
		wireTop.setTextureSize(32, 32);
		wireTop.mirror = true;
		setRotation(wireTop, 0F, 0F, 0F);
		wireMiddle = new ModelRenderer(this, 0, 0);
		wireMiddle.addBox(-6F, -0.5F, -0.5F, 12, 1, 1);
		wireMiddle.setRotationPoint(0F, 15F, 0F);
		wireMiddle.setTextureSize(32, 32);
		wireMiddle.mirror = true;
		setRotation(wireMiddle, 0F, 0F, 0F);
		wireBottom = new ModelRenderer(this, 0, 0);
		wireBottom.addBox(-6F, -0.5F, -0.5F, 12, 1, 1);
		wireBottom.setRotationPoint(0F, 18F, 0F);
		wireBottom.setTextureSize(32, 32);
		wireBottom.mirror = true;
		setRotation(wireBottom, 0F, 0F, 0F);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3,
			float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		postLeft.render(f5);
		postRight.render(f5);
		wireTop.render(f5);
		wireMiddle.render(f5);
		wireBottom.render(f5);
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
